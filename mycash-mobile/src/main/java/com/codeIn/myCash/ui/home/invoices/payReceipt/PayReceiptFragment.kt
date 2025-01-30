package com.codeIn.myCash.ui.home.invoices.payReceipt

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.Style
import com.codeIn.common.nearpay.NearPayPayment
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.payment.NFCChecker
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentPayReceiptBinding
import com.codeIn.myCash.ui.PaymentType
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptModel
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.model.CurrentReceiptModel
import dagger.hilt.android.AndroidEntryPoint
import io.nearpay.sdk.data.models.ReconciliationReceipt
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.ReconcileFailure
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.TransactionData
import javax.inject.Inject

@AndroidEntryPoint
class PayReceiptFragment : Fragment() {

    private val viewModel: PayReceiptViewModel by viewModels()
    private var _binding: FragmentPayReceiptBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPayReceiptBinding.inflate(inflater, container, false)
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

            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            cashTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.CASH)
            }
            creditCardTextView.setOnClickListener {
                if (NFCChecker.checkNotPOSAndNFC(requireContext()))
                    viewModel.updatePaymentType(PaymentType.CREDIT_CARD)
                else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())){
                    CustomToaster.show(
                        requireContext() ,
                        getString(R.string.this_device_is_not_support_nfc) ,
                        false
                    )
                }
            }
            cashAndCreditCardTextView.setOnClickListener {
                if (NFCChecker.checkNotPOSAndNFC(requireContext()))
                    viewModel.updatePaymentType(PaymentType.CASH_AND_CREDIT_CARD)
                else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())){
                    CustomToaster.show(
                        requireContext() ,
                        getString(R.string.this_device_is_not_support_nfc) ,
                        false
                    )
                }
            }

            payInvoiceButton.setOnClickListener {
                var cashValue = cashPaidAmountEditText.text.toString()
                var visaValue = creditCardPaidAmountEditText.text.toString()
                if (viewModel.paymentType.value == PaymentType.CASH)
                    cashValue = viewModel.currentReceiptModel.value?.money?:"0.0"
                if (viewModel.paymentType.value == PaymentType.CREDIT_CARD)
                    visaValue = viewModel.currentReceiptModel.value?.money?:"0.0"

                if (viewModel.paymentType.value == PaymentType.CREDIT_CARD ||
                    viewModel.paymentType.value == PaymentType.CASH_AND_CREDIT_CARD){
                    val value = NumberHelper.persianToEnglishText(visaValue)
                    if (value.toDouble() > 0){
                        if (NFCChecker.checkNotPOSAndNFC(requireContext())) {
                            NearPayPayment.purchaseNearPay(
                                view,value ,
                                "9ace70b7-977d-4094-b7f4-4ecb17de6753" ,
                                ""  , nearPayListener , cashValue , visaValue)
                        } else {
                           CustomToaster.show(
                               requireContext() ,
                               getString(R.string.this_device_is_not_support_nfc),
                               false
                           )
                        }
                    }
                    else
                        viewModel.payReceipt(cashValue , visaValue , viewModel.currentReceiptModel.value?.nextDate)
                }
                else
                    viewModel.payReceipt(cashValue , visaValue , viewModel.currentReceiptModel.value?.nextDate)
            }
        }


        viewModel.apply {
            paymentType.observe(viewLifecycleOwner) { type ->
                this@PayReceiptFragment.updatePaymentType(type = type)
            }

            receiptModel.observe(viewLifecycleOwner){
                binding.bindSummary(it)
            }

            currentReceiptModel.observe(viewLifecycleOwner){
                binding.bindCurrantReceipt(it)
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
                    is ReceiptState.StateError -> handleError(response.message.toString())
                    is ReceiptState.SuccessPayReceipt -> handleSuccess(response.message)
                    is ReceiptState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }
            paymentType.observe(viewLifecycleOwner) { type ->
                updatePaymentType(type = type)
                when(type){
                    PaymentType.CASH_AND_CREDIT_CARD-> handleCashAndCard()
                    else -> {}
                }
            }
        }
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }

    private fun handleSuccess(message : String?){
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().popBackStack()
    }

    private fun updatePaymentType(type: PaymentType) {
        val context = requireContext()
        val binding = binding

        val viewsToStyle = listOf(
            binding.cashTextView,
            binding.postpaidTextView,
            binding.cashAndCreditCardTextView,
            binding.postpaidAndCreditCardTextView,
            binding.installmentPaymentTextView
        )

        binding.apply {
            when (type) {
                PaymentType.CASH_AND_CREDIT_CARD -> {
                    cashPaidAmountEditText.visible()
                    creditCardPaidAmountEditText.visible()
                    remainingLayout.gone()
                    invoicePaymentDateEditText.gone()
                    cashPaidAmountEditText.binding.editText.setText("")
                    creditCardPaidAmountEditText.isEnabled = false
                    creditCardPaidAmountEditText.setText(NumberHelper.persianToEnglishText(
                        viewModel.currentReceiptModel.value?.money?:"0.0"))
                }
                else -> {
                    cashPaidAmountEditText.gone()
                    creditCardPaidAmountEditText.gone()
                    remainingLayout.gone()
                    invoicePaymentDateEditText.gone()
                }
            }
        }
        val selectedView = when (type) {
            PaymentType.CASH -> binding.cashTextView
            PaymentType.CREDIT_CARD -> binding.creditCardTextView
            PaymentType.POST_PAID -> binding.postpaidTextView
            PaymentType.CASH_AND_CREDIT_CARD -> binding.cashAndCreditCardTextView
            PaymentType.POST_PAID_AND_CREDIT_CARD -> binding.postpaidAndCreditCardTextView
            PaymentType.INSTALLMENT -> binding.installmentPaymentTextView
        }

        hideKeyboard(requireContext(), binding.root)


        val (secondaryBg, primaryBg) = R.drawable.bg_white_r12_s5_stroke20_ripple to R.drawable.bg_white_r12_s2_secondary_ripple
        // Define the secondary style to be applied to the views that are not selected
        val secondaryStyle = Style(
            color = ContextCompat.getColor(context, R.color.mainBlack),
            backgroundResource = secondaryBg,
            typeface = ResourcesCompat.getFont(context, R.font.regular)
        )

        // Define the primary style to be applied to the selected view
        val primaryStyle = Style(
            color = ContextCompat.getColor(context, R.color.secondaryColor),
            backgroundResource = primaryBg,
            typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
        )

        // initially apply the secondary style to all the views
        viewsToStyle.forEach { view ->
            view.changeDrawableAndTextColors(
                color = secondaryStyle.color,
                backgroundResource = secondaryStyle.backgroundResource,
                typeface = secondaryStyle.typeface
            )
        }
        binding.creditCardTextView.changeDrawableAndTextColors(
            color = secondaryStyle.color,
            backgroundResource = secondaryStyle.backgroundResource,
            typeface = secondaryStyle.typeface,
            skipDrawable = true
        )

        if (type == PaymentType.CREDIT_CARD) {
            binding.creditCardTextView.changeDrawableAndTextColors(
                color = primaryStyle.color,
                backgroundResource = primaryStyle.backgroundResource,
                typeface = primaryStyle.typeface,
                skipDrawable = true
            )
        } else
        // apply the primary style to the selected view
            selectedView.changeDrawableAndTextColors(
                color = primaryStyle.color,
                backgroundResource = primaryStyle.backgroundResource,
                typeface = primaryStyle.typeface
            )

    }

    private fun navigateTo(navDirections: NavDirections) {
        if (findNavController().currentDestination?.id == R.id.navigation_payReceiptFragment) {
            findNavController().navigate(navDirections)
        }
    }

    private fun FragmentPayReceiptBinding.bindSummary(receiptModel: ReceiptModel){
        invoiceNumberTextView.text = "#${receiptModel?.invoiceData?.invoiceNumber}"
        totalAmountTextView.text = "${receiptModel.invoiceData?.totalPrice} ${viewModel.currency}"
    }

    private fun FragmentPayReceiptBinding.bindCurrantReceipt(currentReceiptModel: CurrentReceiptModel){
        newPaidTextView.text = "${currentReceiptModel.money} ${viewModel.currency}"
        deservedAmountTextView.text = "${currentReceiptModel.remaining} ${viewModel.currency}"
        paymentDateTextView.text = "${currentReceiptModel.nextDate?:"----"}"
    }

    private fun handleCashAndCard(){
        binding.apply {
            cashPaidAmountEditText.binding.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    creditCardPaidAmountEditText.setText(
                        NumberHelper.persianToEnglishText(
                            viewModel.currentReceiptModel.value?.money?:"0.0"))
                }
                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    if (charSequence.isNotEmpty()){
                        if (NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble() <=
                            NumberHelper.persianToEnglishText(viewModel.currentReceiptModel.value?.money?:"0.0").toDouble())
                        {
                            creditCardPaidAmountEditText.setText(
                                NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                                    viewModel.currentReceiptModel.value?.money?:"0.0").toDouble()
                                        - NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble()).toString()))
                        }
                        else{
                            creditCardPaidAmountEditText.setText(
                                NumberHelper.persianToEnglishText(
                                    viewModel.currentReceiptModel.value?.money?:"0.0"))
                            cashPaidAmountEditText.binding.editText.setText("")
                        }
                    }
                    else {
                        creditCardPaidAmountEditText.setText(
                            NumberHelper.persianToEnglishText(
                                viewModel.currentReceiptModel.value?.money?:"0.0"))
                    }
                }
                override fun afterTextChanged(editable: Editable) {}
            })
        }
    }


    private val nearPayListener = object : NearPayPayment.Companion.Listener {
        override fun onPurchaseProcess(
            code: Int,
            transactionData: TransactionData?,
            purchaseFailure: PurchaseFailure?,
            cashValue: String?,
            visaValue: String?
        ) {

            if (code == 0){
                viewModel.payReceipt(cashValue , visaValue ,
                    viewModel.currentReceiptModel.value?.nextDate ,
                    transactionData?.receipts?.get(0)?.transaction_uuid,
                    transactionData?.receipts?.get(0)?.approval_code?.value.toString(),
                    transactionData?.receipts?.get(0)?.created_at,
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

        override fun onRefundProcess(
            code: Int,
            transaction: TransactionData?,
            purchaseFailure: RefundFailure?,
            cashPrice: String?,
            visaPrice: String?,
            paymentType: Int
        ) {
            TODO("Not yet implemented")
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