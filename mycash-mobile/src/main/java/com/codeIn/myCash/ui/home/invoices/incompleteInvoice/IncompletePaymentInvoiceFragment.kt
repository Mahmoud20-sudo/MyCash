package com.codeIn.myCash.ui.home.invoices.incompleteInvoice

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentIncompletePaymentInvoiceBinding
import com.codeIn.myCash.databinding.ItemPaymentBinding
import com.codeIn.myCash.ui.home.clients_vendors.cleint_details.adapters.ReceiptsAdapter
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptModel
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.model.CurrentReceiptModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IncompletePaymentInvoiceFragment : Fragment() {

    private val viewModel: IncompletePaymentInvoiceViewModel by viewModels()

    private var _binding: FragmentIncompletePaymentInvoiceBinding? = null
    private val binding get() = _binding!!

    private var currentReceiptModel : CurrentReceiptModel? = null

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncompletePaymentInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            showAllReceiptsTextView.setOnClickListener {
                navigateTo(
                    com.codeIn.myCash.ui.home.invoices.incompleteInvoice.IncompletePaymentInvoiceFragmentDirections.actionNavigationIncompletePaymentInvoiceFragmentToSeeMoreReceiptsFragment(
                        viewModel.invoiceModel.value?.id.toString()
                    )
                )
            }

        }

        viewModel.apply {
            val receiptsAdapter = ReceiptsAdapter(receiptsCommunicator , currency)
            binding.receiptRecycleView.adapter = receiptsAdapter

            invoiceModel.observe(viewLifecycleOwner) {
                binding.invoiceDetailsLayout.bindInvoice(it)
                getReceipts()
            }

            receiptDataResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is ReceiptState.Loading -> handleLoading()
                    is ReceiptState.Idle -> handleIdle()
                    is ReceiptState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ReceiptState.StateError -> {}
                    is ReceiptState.SuccessShowReceipts -> {
                        if (response.data?.data?.isEmpty() == true) {
                            binding.receiptRecycleView.gone()
                            binding.receiptsLayout.gone()
                        } else {
                            receiptsAdapter.submitList(response?.data?.data)
                            binding.receiptRecycleView.visible()
                            binding.receiptsLayout.visible()
                        }
                    }
                    is ReceiptState.InputError -> {}
                    is ReceiptState.ServerError -> {}
                    else -> {}
                }
            }
        }
    }

    private fun ItemPaymentBinding.bindInvoice(invoice: InvoiceModel) {
        countTextView.text = "${invoice.products?.size?:0}"
        dateTextView.text = invoice.date
        receiptAmountTextView.text = "${invoice.totalPrice} ${viewModel.currency}"
        receiptNumberTextView.text = "#${invoice.invoiceNumber}"
    }

    private fun navigateTo(navDirections: NavDirections) {
        if (findNavController().currentDestination?.id == R.id.navigation_incompletePaymentInvoiceFragment) {
            findNavController().navigate(navDirections)
        }
    }

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
    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()
    private val payReceiptCommunicator = object : PayReceiptDialog.Communicator {
        override fun onPay(model : CurrentReceiptModel , receipt: ReceiptModel) {
            currentReceiptModel = model
            navigateTo(
                com.codeIn.myCash.ui.home.invoices.incompleteInvoice.IncompletePaymentInvoiceFragmentDirections.actionNavigationIncompletePaymentInvoiceFragmentToNavigationPayReceiptFragment(
                    receipt, model
                )
            )
        }


    }

}