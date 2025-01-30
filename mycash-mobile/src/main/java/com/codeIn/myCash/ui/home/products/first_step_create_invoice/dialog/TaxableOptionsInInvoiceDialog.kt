package com.codeIn.myCash.ui.home.products.first_step_create_invoice.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import com.codeIn.common.data.Discount
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogDiscountOptionsInvoiceBinding
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.model.DiscountInInvoiceModel
import com.codeIn.myCash.features.stock.domain.product.usecases.ValidationDiscountUseCase
import javax.inject.Inject

class TaxableOptionsInInvoiceDialog @Inject constructor(
    context: Context,
    private val productModel: ProductModel,
    private val listener: Listener,
    private val currencyTxt : String?,
    private val validationDiscountUseCase: ValidationDiscountUseCase
    ) : Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogDiscountOptionsInvoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogDiscountOptionsInvoiceBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when (productModel.discountInInvoiceModel?.discountType){
            Discount.Value -> {
//                binding.valueRadioButton.isChecked = true
//                binding.discountEditText.isEnabled = true
//                binding.discountEditText.setText(productModel.discountInInvoiceModel?.discountValue ?: "0.0")
                binding.currency.text = currencyTxt
            }
            Discount.Percentage -> {
//                binding.percentageRadioButton.isChecked = true
//                binding.discountEditText.isEnabled = true
//                binding.discountEditText.setText(productModel.discountInInvoiceModel?.discountValue ?: "0.0")
                binding.currency.text = "%"
            }
            Discount.None -> {
//                binding.noneRadioButton.isChecked = true
//                binding.discountEditText.isEnabled = false
//                binding.discountEditText.setText("")
                binding.currency.text = ""
            }
            else -> {}
        }

        binding.apply {
//            percentageRadioButton.setOnClickListener {
//                if (percentageRadioButton.isChecked && productModel.discountInInvoiceModel?.discountType != Discount.Percentage )
//                {
//                    productModel.discountInInvoiceModel?.discountType = Discount.Percentage
//                    currency.text = "%"
//                    discountEditText.isEnabled = true
//                    discountEditText.setText("")
//                    discountEditText.error = null
//                }
//            }
//            valueRadioButton.setOnClickListener {
//                if (valueRadioButton.isChecked && productModel.discountInInvoiceModel?.discountType != Discount.Value )
//                {
//                    productModel.discountInInvoiceModel?.discountType = Discount.Value
//                    currency.text = currencyTxt.toString()
//                    discountEditText.isEnabled = true
//                    discountEditText.setText("")
//                    discountEditText.error = null
//                }
//            }
//            noneRadioButton.setOnClickListener {
//                if (noneRadioButton.isChecked && productModel.discountInInvoiceModel?.discountType != Discount.None )
//                {
//                    productModel.discountInInvoiceModel?.discountType = Discount.None
//                    currency.text = ""
//                    discountEditText.setText("")
//                    discountEditText.error = null
//                    discountEditText.isEnabled = false
//                }
//            }
//            addButton.setOnClickListener {
//                val discount = DiscountInInvoiceModel(
//                    discountType = productModel.discountInInvoiceModel?.discountType ?: Discount.None,
//                    discountValue = NumberHelper.persianToEnglishText(discountEditText.text.toString()))
//                validationDiscountUseCase
//                    .invoke(discount.discountType.value ,
//                        discount.discountValue , productModel.productPriceAfterDiscount).let {
//                        when(it){
//                            InvalidInput.DISCOUNT_VALUE ->{
//                                discountEditText.error = context.getString(R.string.please_enter_valid_discount_value)
//                            }
//                            InvalidInput.EMPTY ->{
//                                discountEditText.error = context.getString(R.string.please_fill_all_the_fields)
//                            }
//                            InvalidInput.DISCOUNT_PERCENTAGE ->{
//                                discountEditText.error = context.getString(R.string.please_enter_valid_discount_percentage)
//                            }
//                            InvalidInput.NONE ->{
//                                discountEditText.error = null
//                                listener.onSaveDiscount(discount)
//                                dismiss()
//                            }
//                            else -> {
//                                discountEditText.error = null
//                            }
//                        }
//                    }
//            }
//            cancelButton.setOnClickListener {
//                dismiss()
//            }
//
//            discountEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
//                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
//                    if (charSequence.isNotEmpty()){
//                        validationDiscountUseCase
//                            .invoke(productModel.discountInInvoiceModel?.discountType?.value ?: Discount.None.value,
//                                charSequence.toString() , productModel.productPriceAfterDiscount).let {
//                                when(it){
//                                    InvalidInput.DISCOUNT_VALUE ->{
//                                        discountEditText.error = context.getString(R.string.please_enter_valid_discount_value)
//                                    }
//                                    InvalidInput.EMPTY ->{
//                                        discountEditText.error = context.getString(R.string.please_fill_all_the_fields)
//                                    }
//                                    InvalidInput.DISCOUNT_PERCENTAGE ->{
//                                        discountEditText.error = context.getString(R.string.please_enter_valid_discount_percentage)
//                                    }
//                                    InvalidInput.NONE ->{
//                                        discountEditText.error = null
//                                    }
//                                    else -> {
//                                        discountEditText.error = null
//                                    }
//                                }
//                            }
//                    }
//                }
//                override fun afterTextChanged(editable: Editable) {}
//            })
        }


    }

    interface Listener {
        fun onSaveDiscount(discount: DiscountInInvoiceModel)
    }
}