package com.codeIn.myCash.ui.home.clients_vendors.memorandum_invoice_payment

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
import com.codeIn.common.data.PaymentType
import com.codeIn.common.data.Style
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentInvoicePaymentBinding
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.invoices.memorandumInvoicePayment.MemorandumInvoicePaymentViewModel
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientMemorandumInvoicePaymentFragment : Fragment()  {


    private val viewModel: MemorandumInvoicePaymentViewModel by viewModels()
    private var _binding: FragmentInvoicePaymentBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()
    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoicePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //cancel operation and pop the back stack
        binding.apply {
            cancelButton.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("products_in_memorandum",
                    viewModel.selectedProductsInMemorandum.value?:ProductInInvoiceMemorandum())
                findNavController().popBackStack()
            }
            backArrow.setOnClickListener {
                Log.d("TAG" , "Here 11")
                findNavController().previousBackStackEntry?.savedStateHandle?.set("products_in_memorandum",
                    viewModel.selectedProductsInMemorandum.value?:ProductInInvoiceMemorandum())
                Log.d("TAG" , "Here 12")
                findNavController().popBackStack()
            }
        }


        binding.apply {
            cashTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.CASH)
            }
            creditCardTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.CREDIT_CARD)
            }
            cashAndCreditCardTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.CASH_AND_CREDIT_CARD)
            }
            proceedButton.setOnClickListener {
                var cashValue = cashPaidAmountEditText.text.toString()
                var visaValue = creditCardPaidAmountEditText.text.toString()
                if (viewModel.paymentType.value == PaymentType.CASH)
                    cashValue = viewModel.selectedProductsInMemorandum.value?.finalTotal?:"0.0"
                if (viewModel.paymentType.value == PaymentType.CREDIT_CARD)
                    visaValue = viewModel.selectedProductsInMemorandum.value?.finalTotal?:"0.0"

                viewModel.makeMemorandum(cashValue , visaValue , viewModel.paymentType.value?.value.toString())
            }
        }



        viewModel.apply {
            paymentType.observe(viewLifecycleOwner) { type ->
                this@ClientMemorandumInvoicePaymentFragment.updatePaymentType(type = type)
                when(type){
                    PaymentType.CASH_AND_CREDIT_CARD-> handleCashAndCard()
                    else -> {}
                }
            }

            selectedProductsInMemorandum.observe(viewLifecycleOwner){
                binding.showPrices(it)
            }

            memorandumDataResult.observe(
                viewLifecycleOwner) { response ->
                when (response) {
                    is MemorandumState.Loading -> handleLoading()
                    is MemorandumState.Idle -> handleIdle()
                    is MemorandumState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is MemorandumState.StateError -> handleError(response.message.toString())
                    is MemorandumState.SuccessShowSingleMemorandum -> handleSuccess()
                    is MemorandumState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
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

    private fun handleSuccess(){
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().popBackStack()
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
            cashPaidAmountEditText.gone()
            creditCardPaidAmountEditText.gone()
            remainingLayout.gone()
            invoicePaymentDateEditText.gone()
        }
        val selectedView = when (type) {
            PaymentType.CASH -> {
                binding.cashTextView
            }
            PaymentType.CREDIT_CARD -> {
                binding.creditCardTextView
            }
            PaymentType.POST_PAID -> {
                binding.apply {
                    cashPaidAmountEditText.visible()
                    remainingLayout.visible()
                    invoicePaymentDateEditText.visible()
                }
                binding.postpaidTextView
            }
            PaymentType.CASH_AND_CREDIT_CARD -> {
                binding.apply {
                    cashPaidAmountEditText.visible()
                    creditCardPaidAmountEditText.visible()
                    cashPaidAmountEditText.setText("")
                    creditCardPaidAmountEditText.isEnabled = false
                    creditCardPaidAmountEditText.setText(NumberHelper.persianToEnglishText(
                        viewModel.selectedProductsInMemorandum.value?.finalTotal?:"0.0"))
                }
                binding.cashAndCreditCardTextView
            }
            PaymentType.POST_PAID_AND_CREDIT_CARD -> {
                binding.postpaidAndCreditCardTextView
            }
            PaymentType.INSTALLMENT -> {
                binding.installmentPaymentTextView
            }
        }

        hideKeyboard(requireContext(),binding.root)

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


  private fun FragmentInvoicePaymentBinding.showPrices(it : ProductInInvoiceMemorandum?) {
      initialTotalTextView.text = "${it?.initialTotal} ${viewModel.currency}"
      vatLabel.text = "${getString(R.string.added_tax)} (${viewModel?.tax})"
      vat15TextView.text = "${it?.tax} ${viewModel.currency}"
      totalPriceTextView.text = "${it?.finalTotal} ${viewModel.currency}"
    }

    private fun handleCashAndCard(){
        binding.apply {
            cashPaidAmountEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    creditCardPaidAmountEditText.setText(
                        NumberHelper.persianToEnglishText(
                            viewModel.selectedProductsInMemorandum.value?.finalTotal?:"0.0"))
                }
                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    if (charSequence.isNotEmpty()){
                        if (NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble() <=
                            NumberHelper.persianToEnglishText(viewModel.selectedProductsInMemorandum.value?.finalTotal?:"0.0").toDouble())
                        {
                            creditCardPaidAmountEditText.setText(
                                NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                                    viewModel.selectedProductsInMemorandum.value?.finalTotal?:"0.0").toDouble()
                                        - NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble()).toString()))
                        }
                        else{
                            creditCardPaidAmountEditText.setText(
                                NumberHelper.persianToEnglishText(
                                    viewModel.selectedProductsInMemorandum.value?.finalTotal?:"0.0"))
                            cashPaidAmountEditText.setText("")
                        }
                    }
                    else {
                        creditCardPaidAmountEditText.setText(
                            NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInMemorandum.value?.finalTotal?:"0.0"))
                    }
                }
                override fun afterTextChanged(editable: Editable) {}
            })
        }
    }

    override fun onResume() {
        super.onResume()
    }
}