package com.codeIn.myCash.ui.home.clients_vendors.vendor_details

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
import com.codeIn.myCash.databinding.FragmentVendorsDetailsBinding
import com.codeIn.myCash.ui.home.clients_vendors.cleint_details.adapters.PaymentsAdapter
import com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.Payment
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.ui.home.invoices.invoices.InvoicesAdapter
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VendorDetailsFragment : Fragment() {

    private val viewModel: VendorDetailsViewModel by viewModels()
    private var _binding: FragmentVendorsDetailsBinding? = null
    private val binding get() = _binding!!

    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVendorsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback() {
//            findNavController().popBackStack()
        }
        //observe the vendorsData from the view model and update the UI accordingly
        viewModel.apply {

            val invoicesAdapter = InvoicesAdapter(requireContext(), invoicesCommunicator , viewModel.currency)
            binding.purchasesRecycleView.adapter = invoicesAdapter

            vendorDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ClientState.SuccessDeleteClient-> {
                        handleSuccessDeleteVendor(response.message)
                    }
                    is ClientState.SuccessShowSingleClient ->{
                        updateVendor(response.data)
                    }
                    is ClientState.Loading -> handleLoading()
                    is ClientState.Idle -> handleIdle()
                    is ClientState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ClientState.InputError -> {
                    }
                    is ClientState.StateError -> {
                        handleError(response.message.toString())
                    }
                    is ClientState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else ->{

                    }
                }
            }

            invoicesDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is InvoiceState.SuccessShowInvoices ->{
                        if (response.data?.data?.isNotEmpty() == true) {
                            invoicesAdapter.submitList(response.data?.data)
                            binding.purchasesRecycleView.visible()
                            binding.invoicesLayout.visible()
                            binding.invoicesCounter.text = "${getString(R.string.invoices)} (${response.data?.data?.size?:0})"
                        } else {
                            invoicesAdapter.submitList(response.data?.data)
                            binding.purchasesRecycleView.gone()
                            binding.invoicesLayout.gone()
                        }
                    }
                    is InvoiceState.Loading -> handleLoading()
                    is InvoiceState.Idle -> handleIdle()
                    is InvoiceState.UnAuthorized ->{
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is InvoiceState.InputError -> {
                    }
                    is InvoiceState.StateError -> {
                        handleError(response.message.toString())
                    }
                    is InvoiceState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else ->{

                    }
                }
            }


            vendor.collectOnLifecycle(viewModelScope , viewLifecycleOwner){vendor->
                handleSuccessSingleVendor(vendor)
            }

        }


        binding.apply {
            deleteVendorButton.setOnClickListener {
                viewModel.deleteVendor()
            }

            editImageView.setOnClickListener {
                findNavController().navigate(
                    com.codeIn.myCash.ui.home.clients_vendors.vendor_details.VendorDetailsFragmentDirections.actionNavigationVendorsDetailsFragmentToUpdateVendorFragment(
                        viewModel.vendor.value ?: ClientModel(
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
                    com.codeIn.myCash.ui.home.clients_vendors.vendor_details.VendorDetailsFragmentDirections.actionNavigationVendorsDetailsFragmentToSeeMoreInvoicesFragment(
                        viewModel.vendor.value?.id.toString()
                    )
                )
            }
        }

    }

    private val invoicesCommunicator = object : InvoicesAdapter.Communicator {
        override fun onClick(invoice: InvoiceModel) {
            findNavController().navigate(
                com.codeIn.myCash.ui.home.clients_vendors.vendor_details.VendorDetailsFragmentDirections.actionNavigationVendorsDetailsFragmentToInvoiceFragment(
                    invoice.id.toString()
                )
            )
        }

        override fun onUnpaidClick(invoice: InvoiceModel) {
            TODO("Not yet implemented")
        }
    }



    private fun handleSuccessSingleVendor(vendorModel: ClientModel?){
        viewModel.clearVendorState()
        binding.loadingLayout.root.gone()
        binding.apply {
            nameTextView.text = vendorModel?.name?:"----"
            phoneNumberTextView.text = vendorModel?.phone
            commercialRegistrationTextView.text = vendorModel?.commercialRecord?:"----"
            taxNumberTextView.text = vendorModel?.taxRecord?:"----"
            clientNumberTextView.text = "#${vendorModel?.id.toString()}"
            addressTextView.text = vendorModel?.address?:"----"
        }
    }

    private fun handleSuccessDeleteVendor(message: String?){
        viewModel.clearVendorState()
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
        viewModel.clearVendorState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }


    override fun onResume() {
        super.onResume()
        if (findNavController().currentBackStackEntry?.savedStateHandle?.contains("vendor") == true)
            viewModel.updateVendor(findNavController().currentBackStackEntry?.savedStateHandle?.get("vendor") ?: null )

    }

}