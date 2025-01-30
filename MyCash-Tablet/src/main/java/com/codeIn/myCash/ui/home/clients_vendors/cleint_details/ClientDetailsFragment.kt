package com.codeIn.myCash.ui.home.clients_vendors.cleint_details

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.databinding.FragmentClientsDetailsBinding
import com.codeIn.myCash.ui.home.clients_vendors.cleint_details.adapters.PaymentsAdapter
import com.codeIn.myCash.ui.home.clients_vendors.cleint_details.adapters.ReceiptsAdapter
import com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.Payment
import com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.Receipt
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.ui.home.clients_vendors.see_more_receipts_client.SeeMoreReceiptsClientFragmentDirections
import com.codeIn.myCash.ui.home.invoices.incompleteInvoice.PayReceiptDialog
import com.codeIn.myCash.ui.home.invoices.invoices.InvoicesAdapter
import com.codeIn.myCash.ui.home.invoices.invoices.InvoicesFragmentDirections
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptModel
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.receipt.model.CurrentReceiptModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientDetailsFragment : Fragment() {

    private val viewModel: ClientDetailsViewModel by viewModels()
    private val infoDialog: InfoDialog = InfoDialog()
    private var _binding: FragmentClientsDetailsBinding? = null
    private val binding get() = _binding!!
    private var currentReceiptModel : CurrentReceiptModel? = null

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {

            val invoicesAdapter = InvoicesAdapter(requireContext(), invoicesCommunicator , viewModel.currency)
            binding.paymentsRecycleView.adapter = invoicesAdapter

            val receiptsAdapter = ReceiptsAdapter(receiptsCommunicator , viewModel.currency)
            binding.receiptRecycleView.adapter = receiptsAdapter

            clientDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ClientState.SuccessDeleteClient-> {
                        handleSuccessDeleteClient(response.message)
                    }
                    is ClientState.SuccessShowSingleClient ->{
                        updateClient(response.data)
                    }
                    is ClientState.Loading -> handleLoading()
                    is ClientState.Idle -> handleIdle()
                    is ClientState.InputError -> {
                    }
                    is ClientState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    is ClientState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }else ->{

                    }
                }
            }

            invoicesDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is InvoiceState.SuccessShowInvoices ->{
                        if (response.data?.data?.isNotEmpty() == true) {
                            invoicesAdapter.submitList(response.data?.data)
                            binding.paymentsRecycleView.visible()
                            binding.invoicesLayout.visible()
                            binding.invoicesCounter.text = "${getString(R.string.invoices)} (${response.data?.data?.size?:0})"
                        } else {
                            invoicesAdapter.submitList(response.data?.data)
                            binding.paymentsRecycleView.gone()
                            binding.invoicesLayout.gone()
                        }
                    }
                    is InvoiceState.Loading -> handleLoading()
                    is InvoiceState.Idle -> handleIdle()
                    is InvoiceState.InputError -> {
                    }
                    is InvoiceState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else ->{

                    }
                }
            }

            receiptsDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ReceiptState.SuccessShowReceipts ->{
                        if (response.data?.data?.isNotEmpty() == true) {
                            receiptsAdapter.submitList(response.data?.data)
                            binding.receiptsCounter.text ="${getString(R.string.receipts)} (${response.data?.data?.size?:0})"
                            binding.receiptRecycleView.visible()
                            binding.receiptsLayout.visible()
                        } else {
                            receiptsAdapter.submitList(response.data?.data)
                            binding.receiptRecycleView.gone()
                            binding.receiptsLayout.gone()
                        }
                    }
                    is ReceiptState.Loading -> handleLoading()
                    is ReceiptState.Idle -> handleIdle()
                    is ReceiptState.InputError -> {}
                    is ReceiptState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else ->{

                    }
                }
            }
            client.collectOnLifecycle(viewModelScope , viewLifecycleOwner){client->
                handleSuccessSingleClient(client)
                getInvoices()
                getReceipts()
            }

        }
        binding.apply {
            deleteClientButton.setOnClickListener {
                viewModel.deleteClient()
            }

            editImageView.setOnClickListener {
                findNavController().navigate(
                    com.codeIn.myCash.ui.home.clients_vendors.cleint_details.ClientDetailsFragmentDirections.actionNavigationClientsDetailsFragmentToUpdateClientFragment(
                        viewModel.client.value ?: ClientModel(
                            address = null,
                            email = null,
                            name = null,
                            phone = null,
                            commercialRecord = null,
                            taxRecord = null,
                            id = null,
                            notes = null,
                            totalUnPaid = null
                        )
                    )
                )
            }

            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            showAllTextView.setOnClickListener {
                findNavController().navigate(
                    com.codeIn.myCash.ui.home.clients_vendors.cleint_details.ClientDetailsFragmentDirections.actionNavigationClientsDetailsFragmentToSeeMoreInvoicesFragment(
                        viewModel.client?.value?.id.toString()
                    )
                )
            }

            showAllReceipts.setOnClickListener {
                findNavController().navigate(
                    com.codeIn.myCash.ui.home.clients_vendors.cleint_details.ClientDetailsFragmentDirections.actionNavigationClientsDetailsFragmentToSeeMoreReceiptsClientFragment(
                        viewModel.client.value?.id.toString()
                    )
                )
            }
        }
    }


    private fun handleSuccessSingleClient(client : ClientModel?){
        viewModel.clearClientState()
        binding.apply {
            nameTextView.text = client?.name?:"----"
            phoneNumberTextView.text = client?.phone
            commercialRegistrationTextView.text = client?.commercialRecord?:"----"
            taxNumberTextView.text = client?.taxRecord?:"----"
            clientNumberTextView.text = "#${client?.id.toString()}"
            addressTextView.text = client?.address?:"----"
        }
    }

    private fun handleSuccessDeleteClient(message: String?){
        viewModel.clearClientState()
        CustomToaster.show(
            context = requireContext(),
            message = message!!,
            isSuccess = true
        )
        findNavController().popBackStack()
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        viewModel.clearClientState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }


    override fun onResume() {
        super.onResume()
        if (findNavController().currentBackStackEntry?.savedStateHandle?.contains("client") == true)
            viewModel.updateClient(findNavController().currentBackStackEntry?.savedStateHandle?.get("client") ?: null )

    }
    private val invoicesCommunicator = object : InvoicesAdapter.Communicator {
        override fun onClick(invoice: InvoiceModel) {
            findNavController().navigate(
                com.codeIn.myCash.ui.home.clients_vendors.cleint_details.ClientDetailsFragmentDirections.actionNavigationClientsDetailsFragmentToInvoiceFragment(
                    invoice.id.toString()
                )
            )
        }
    }

    /**
     * Communicator interfaces for the adapters that will be used to communicate with the fragment when the user clicks on a receipt
     */
    private val receiptsCommunicator = object : ReceiptsAdapter.Communicator {
        override fun payReceipt(receipt: ReceiptModel) {
            showPayReceiptDialog(receipt)
        }
    }


    private var payReceiptDialog: PayReceiptDialog? = null
    private fun showPayReceiptDialog(receipt: ReceiptModel) {
        if (payReceiptDialog?.isShowing == true)
            payReceiptDialog?.dismiss()
        payReceiptDialog = PayReceiptDialog(requireContext(), receipt, viewModel.currency
            , parentFragmentManager , payReceiptCommunicator , currentReceiptModel)
        payReceiptDialog?.show()
    }

    private val payReceiptCommunicator = object : PayReceiptDialog.Communicator {
        override fun onPay(model : CurrentReceiptModel, receipt: ReceiptModel) {
            currentReceiptModel = model

            findNavController().navigate(
                com.codeIn.myCash.ui.home.clients_vendors.cleint_details.ClientDetailsFragmentDirections.actionNavigationClientsDetailsFragmentToPayReceiptInvoiceFragment(
                    receipt, model
                )
            )
        }
    }
}