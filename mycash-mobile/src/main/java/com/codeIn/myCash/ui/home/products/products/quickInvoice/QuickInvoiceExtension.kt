package com.codeIn.myCash.ui.home.products.products.quickInvoice

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.PaymentType
import com.codeIn.myCash.databinding.BottomSheetQuickInvoiceBinding
import com.codeIn.myCash.ui.ViewStrokes
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.ui.showError
import com.codeIn.myCash.ui.home.products.products.ProductsFragment
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import com.codeIn.myCash.ui.setErrorMsg

fun ProductsFragment.updateInvoiceType(
    invoiceType: InvoiceType,
    binding: BottomSheetQuickInvoiceBinding ,
) {
    val context = requireContext()

    val viewsToStyle = listOf(
        binding.simpleInvoiceTextView,
        binding.taxInvoiceTextView,
        binding.purchaseInvoiceTextView
    )

    val selectedView = when (invoiceType) {
        InvoiceType.SIMPLE -> binding.simpleInvoiceTextView
        else -> binding.taxInvoiceTextView
    }
    updateSectionsViews(context, viewsToStyle, selectedView, stroke = ViewStrokes.R12_S1)
}

fun ProductsFragment.changeTypeColors(
    type: PaymentType,
    binding: BottomSheetQuickInvoiceBinding,
    viewModel: QuickInvoiceViewModel

) {
    val context = requireContext()
    val viewsToStyle = listOf(
        binding.cashTextView,
        binding.creditCardTextView
    )

    binding.apply {
        when (type) {
            PaymentType.POST_PAID -> {
                cashPaidAmountEditText.visible()
                remainingLayout.visible()
                invoicePaymentDateEditText.visible()
                creditCardPaidAmountEditText.gone()
                cashPaidAmountEditText.setText("")
                creditCardPaidAmountEditText.setText( "")
                remainingTextView.text = NumberHelper.persianToEnglishText(
                    viewModel.selectedProductsInCart.value?.finalTotal?:"0.0")
            }
            PaymentType.CASH_AND_CREDIT_CARD -> {
                cashPaidAmountEditText.visible()
                creditCardPaidAmountEditText.visible()
                remainingLayout.gone()
                invoicePaymentDateEditText.gone()
                cashPaidAmountEditText.setText("")
                remainingTextView.text = ""
                creditCardPaidAmountEditText.isEnabled = false
                creditCardPaidAmountEditText.setText(NumberHelper.persianToEnglishText(
                    viewModel.selectedProductsInCart.value?.finalTotal?:"0.0"))
            }
           PaymentType.POST_PAID_AND_CREDIT_CARD->{
                cashPaidAmountEditText.visible()
                creditCardPaidAmountEditText.visible()
                remainingLayout.visible()
                invoicePaymentDateEditText.visible()
                cashPaidAmountEditText.setText("")
                creditCardPaidAmountEditText.isEnabled = true
                creditCardTextView.text = ""
                remainingTextView.text = NumberHelper.persianToEnglishText(
                    viewModel.selectedProductsInCart.value?.finalTotal?:"0.0")
            }
            else -> {
                cashPaidAmountEditText.gone()
                creditCardPaidAmountEditText.gone()
                remainingLayout.gone()
                invoicePaymentDateEditText.gone()
                cashPaidAmountEditText.setText("")
                creditCardPaidAmountEditText.setText("")
            }
        }
    }
    val selectedView = when (type) {
        PaymentType.CASH -> binding.cashTextView
        else -> binding.creditCardTextView
    }

    updateSectionsViews(
        context = context,
        viewsToStyle = viewsToStyle,
        selectedView = selectedView,
        stroke = ViewStrokes.R12_S5
    )

    hideKeyboard(requireContext(),binding.root)

}

fun ProductsFragment.showSummary(
    binding: BottomSheetQuickInvoiceBinding,
    productsInCart : ProductsInCartInQuickInvoice? ,
    currency : String? ,
    tax : String?
){
    binding.apply {
        initialTotalTextView.text = "${productsInCart?.initialTotal ?: "0.0"} $currency"
        vat15TextView.text = "${productsInCart?.tax ?: "0.0"} $currency"
        totalPriceTextView.text = "${productsInCart?.finalTotal ?: "0.0"} $currency"
        vatLabel.text = "${getString(R.string.tax)} (${tax}%)"
    }
}

fun ProductsFragment.handlePostPaid(
    binding: BottomSheetQuickInvoiceBinding,
    viewModel: QuickInvoiceViewModel
){
    binding.apply {
        cashPaidAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                remainingTextView.text = NumberHelper.persianToEnglishText(
                    viewModel.selectedProductsInCart.value?.finalTotal?:"0.0")
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                if (charSequence.isNotEmpty()){
                    if (NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble() <=
                        NumberHelper.persianToEnglishText(viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble())
                    {
                        remainingTextView.text =
                            NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()
                                    - NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble()).toString())
                    }
                    else{
                        remainingTextView.text = NumberHelper.persianToEnglishText(
                            viewModel.selectedProductsInCart.value?.finalTotal?:"0.0")
                        cashPaidAmountEditText.setText("")
                    }
                }
                else {
                    remainingTextView.text = NumberHelper.persianToEnglishText(
                        viewModel.selectedProductsInCart.value?.finalTotal?:"0.0")
                }
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }
}

fun ProductsFragment.handleCashAndCard(
    binding: BottomSheetQuickInvoiceBinding,
    viewModel: QuickInvoiceViewModel
){
    binding.apply {
        cashPaidAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                creditCardPaidAmountEditText.setText(NumberHelper.persianToEnglishText(
                    viewModel.selectedProductsInCart.value?.finalTotal?:"0.0"))
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                if (charSequence.isNotEmpty()){
                    if (NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble() <=
                        NumberHelper.persianToEnglishText(viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble())
                    {
                        creditCardPaidAmountEditText.setText(
                            NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()
                                    - NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble()).toString()))
                    }
                    else{
                        creditCardPaidAmountEditText.setText(NumberHelper.persianToEnglishText(
                            viewModel.selectedProductsInCart.value?.finalTotal?:"0.0"))
                        cashPaidAmountEditText.setText("")
                    }
                }
                else {
                    creditCardPaidAmountEditText.setText(NumberHelper.persianToEnglishText(
                        viewModel.selectedProductsInCart.value?.finalTotal?:"0.0"))
                }
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }
}

fun ProductsFragment.handlePostPaidAndCard(
    binding: BottomSheetQuickInvoiceBinding,
    viewModel: QuickInvoiceViewModel
){
    binding.apply {
        cashPaidAmountEditText.addTextChangedListener(object : TextWatcher {
            var visaValue = "0.0"
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                if (creditCardPaidAmountEditText.text.isNotEmpty())
                    visaValue = creditCardPaidAmountEditText.text.toString()
                remainingTextView.text =
                    NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                        viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()
                            - NumberHelper.persianToEnglishText(visaValue).toDouble()).toString())
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                if (charSequence.isNotEmpty()){
                    if ((NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble())+visaValue.toDouble() <=
                        NumberHelper.persianToEnglishText(viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble())
                    {
                        remainingTextView.text =
                            NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()
                                    - (NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble()
                                    +visaValue.toDouble())).toString())
                    }
                    else{
                        remainingTextView.text = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                            viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()-visaValue.toDouble()).toString())
                        cashPaidAmountEditText.setText("")
                    }
                }
                else {
                    remainingTextView.text = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                        viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()-visaValue.toDouble()).toString())

                }
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        creditCardPaidAmountEditText.addTextChangedListener(object : TextWatcher {
            var cashValue = "0.0"
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                if (cashPaidAmountEditText.text.isNotEmpty())
                    cashValue = cashPaidAmountEditText.text.toString()
                else
                    remainingTextView.text =
                        NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                            viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()
                                - NumberHelper.persianToEnglishText(cashValue).toDouble()).toString())
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                if (charSequence.isNotEmpty()){
                    if ((NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble())+cashValue.toDouble() <=
                        NumberHelper.persianToEnglishText(viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble())
                    {
                        remainingTextView.text =
                            NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()
                                    - (NumberHelper.persianToEnglishText(charSequence.toString()?:"0.0").toDouble()
                                    +cashValue.toDouble())).toString())
                    }
                    else{
                        remainingTextView.text = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                            viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()-cashValue.toDouble()).toString())
                        creditCardPaidAmountEditText.setText("")
                    }
                }
                else {
                    remainingTextView.text = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(
                        viewModel.selectedProductsInCart.value?.finalTotal?:"0.0").toDouble()-cashValue.toDouble()).toString())

                }
            }
            override fun afterTextChanged(editable: Editable) {}
        })

    }

}

fun ProductsFragment.handleInputErrorInTopSheet(it: InvalidInput,
                                                binding: BottomSheetQuickInvoiceBinding,
                                                isNewItem : Boolean = true ){
    binding.loadingLayout.root.gone()
    when(it){
        InvalidInput.PRICE -> binding.priceEditText.setErrorMsg(getString(R.string.please_enter_valid_price))
        InvalidInput.QUANTITY -> binding.amountEditText.setErrorMsg(getString(R.string.please_enter_valid_quantity))
        InvalidInput.EMPTY -> Toast.makeText(requireContext(), getString(R.string.please_fill_all_the_fields), Toast.LENGTH_SHORT).show()
        InvalidInput.NONE->{
            if (isNewItem)
                this.addProductInQuickInvoice()
        }
        else ->{}
    }
}