package com.codeIn.myCash.ui.home.invoices.invoiceReturn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.Cart
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.PaymentMethods
import com.codeIn.common.data.PaymentStatus
import com.codeIn.common.data.PaymentType
import com.codeIn.common.nearpay.NearPayPayment
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.payment.NFCChecker
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentInvoiceReturnBinding
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.dialogs.PaymentMethodsDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import dagger.hilt.android.AndroidEntryPoint
import io.nearpay.sdk.data.models.ReconciliationReceipt
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.ReconcileFailure
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.TransactionData
import javax.inject.Inject

@AndroidEntryPoint
class InvoiceReturnFragment : Fragment() {

    private val viewModel: InvoiceReturnViewModel by viewModels()
    private var _binding: FragmentInvoiceReturnBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceReturnBinding.inflate(inflater, container, false)
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
            invoiceReturnButton.setOnClickListener {
                var returnedValue = 0.0
                val invoiceTotal = NumberHelper.persianToEnglishText(viewModel.invoiceModel.value?.totalPrice?:"0.0")
                val currentTotal = NumberHelper.persianToEnglishText(viewModel.selectedProductsInReturn.value?.finalTotal?:"0.0")
                val remaining = NumberHelper.persianToEnglishText(viewModel.invoiceModel.value?.rammingPrice?:"0.0")
                val deposit = invoiceTotal.toDouble() - remaining.toDouble()
                returnedValue = invoiceTotal.toDouble() - currentTotal.toDouble()
                if (returnedValue > 0){
                    if (viewModel.invoiceModel.value?.paymentStatus == PaymentStatus.COMPLETED.value.toString()){
                        showPaymentMethodsDialog(returnedValue.toString())
                    }
                    else {
                        if (deposit >= currentTotal.toDouble()){
                            returnedValue = deposit - currentTotal.toDouble()
                            showPaymentMethodsDialog(returnedValue.toString())
                        }
                        else {
                            viewModel.returnInvoice(
                                "0.0" , "0.0" ,
                                viewModel.invoiceModel.value?.paymentType?.toInt()?:0
                            )
                        }
                    }
                }
                else {
                    Toast.makeText(requireContext() , getString(R.string.you_donot_change_in_invoice) , Toast.LENGTH_LONG).show()
                }

            }
        }

        viewModel.apply {
            val adapter = InvoiceProductsAdapter(requireContext() , productCommunicator)
            binding.productRecycleView.adapter = adapter

            invoiceModel.observe(viewLifecycleOwner){
                bindInvoiceData(it)
                if (it.products?.isNotEmpty() == true)
                {
                    updateProductsInInvoice(it.products as ArrayList<ProductInInvoiceModel>)
                    updateSelectedProductsInReturn(it)
                }
            }
            products.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            selectedProductsInReturn.observe(viewLifecycleOwner){
                if (it != null )
                    handleProductInInvoice(null , Cart.INITIAL)
                bindDataSummary(it)
            }

            invoiceDataResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is InvoiceState.Loading -> handleLoading()
                    is InvoiceState.Idle -> handleIdle()
                    is InvoiceState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is InvoiceState.StateError -> handleError(response.message.toString())
                    is InvoiceState.SuccessShowSingleInvoice -> handleInvoiceSuccess(response.data)
                    is InvoiceState.Sucess -> {}
                    is InvoiceState.InputError -> {}
                    is InvoiceState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }
        }
    }

    private fun handleInvoiceSuccess(invoiceModel: InvoiceModel?){
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().popBackStack()
    }
    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        binding.loadingLayout.root.gone()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }


    private fun bindInvoiceData(invoiceModel : InvoiceModel?){
        binding.apply {
            invoiceNumberTextView.text = "#${invoiceModel?.invoiceNumber}"
            invoiceDateTextView.text = "${invoiceModel?.date}"
        }
    }
    private fun bindDataSummary(productsInCart: ProductInReturnInvoice?){
        binding.apply {
            totalAfterDiscountTextView.text = "${productsInCart?.totalAfterDiscount} ${viewModel.currency}"

            initialTotalTextView.text = "${productsInCart?.initialTotal} ${viewModel.currency}"

            discountTextView.text = "${productsInCart?.discount} ${viewModel.currency}"

            vatLabel.text = "${getString(R.string.added_tax)} (${viewModel?.tax})"

            vat15TextView.text = "${productsInCart?.tax} ${viewModel.currency}"

            totalPriceTextView.text = "${productsInCart?.finalTotal} ${viewModel.currency}"
        }
    }

    private val productCommunicator = object : InvoiceProductsAdapter.Communicator {
        override fun removeProductFromInvoice(product: ProductInInvoiceModel) =
            viewModel.handleProductInInvoice(product , Cart.DELETE)

        override fun updateProductInInvoice(product: ProductInInvoiceModel) =
            viewModel.handleProductInInvoice(product, Cart.ADD)
    }

    //we keep a reference to the dialog so we can dismiss it when user clicks multiple times to show it.
    private var paymentMethodsDialog: PaymentMethodsDialog? = null
    private fun showPaymentMethodsDialog(returnedValue : String?) {
        if (paymentMethodsDialog?.isShowing == true)
            paymentMethodsDialog?.dismiss()
        paymentMethodsDialog =
            PaymentMethodsDialog(requireContext(), returnedValue?:"0.0",
                invoiceModel = viewModel.invoiceModel.value ,currency = viewModel.currency ,
                paymentMethodsCommunicator)
        paymentMethodsDialog?.show()
    }

    private val paymentMethodsCommunicator = object : PaymentMethodsDialog.Communicator {
        override fun onPaymentMethodSelected(paymentType: PaymentType, returnedCash : String?, returnedVisa : String?) {
            if (paymentType == PaymentType.CREDIT_CARD || paymentType == PaymentType.CASH_AND_CREDIT_CARD){
                if (NFCChecker.checkNotPOSAndNFC(requireContext())) {
                    val value = NumberHelper.persianToEnglishText(returnedVisa?:"0.0")
                    if (value.toDouble() > 0){
                        NearPayPayment.refundNearPay(
                            view!!, "", value ,
                            viewModel.invoiceModel.value?.runRefund?:"",
                            "9ace70b7-977d-4094-b7f4-4ecb17de6753",
                            nearPayListener , returnedCash , returnedVisa , paymentType.value)
                    }
                }
                else {
                    CustomToaster.show(
                        requireContext() ,
                        getString(R.string.this_device_is_not_support_nfc) ,
                        false
                    )
                }
            }
            else {
                viewModel.returnInvoice(
                    returnedCash , returnedVisa , paymentType.value
                )
            }
        }
    }


    private val nearPayListener = object : NearPayPayment.Companion.Listener{


        override fun onPurchaseProcess(
            code: Int,
            transactionData: TransactionData?,
            purchaseFailure: PurchaseFailure?,
            cashPrice: String?,
            visaPrice: String?
        ) {

        }

        override fun onRefundProcess(
            code: Int,
            transaction: TransactionData?,
            purchaseFailure: RefundFailure?,
            cashPrice: String?,
            visaPrice: String?,
            paymentType: Int
        ) {
            if (code == 0){
                viewModel.returnInvoice(
                    cashPrice , visaPrice , paymentType , transaction?.receipts?.get(0)?.approval_code?.value.toString(),
                    transaction?.receipts?.get(0)?.transaction_uuid,  transaction?.receipts?.get(0)?.created_at,
                )
            }
            else
            {
                when (code) {
                    1 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }
                    2 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }
                    3 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.authentication_failed),
                            false
                        )
                    }
                    4 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.general_error),
                            false
                        )
                    }
                    5 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.general_error),
                            false
                        )
                    }
                }
            }
        }

        override fun onReconciliationProcess(
            code: Int,
            receipt: ReconciliationReceipt?,
            reconcileFailure: ReconcileFailure?
        ) {
            TODO("Not yet implemented")
        }

    }

}