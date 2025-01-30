package com.codeIn.myCash.ui.home.invoices.makeMemorandum

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import com.codeIn.common.domain.usecases.MoneyValidationUseCase
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogSpecifyAmountBinding
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import javax.inject.Inject


class SpecifyAmountDialog @Inject constructor(
    context: Context,
    private val communicator: Communicator,
    private val productModel: ProductModel,
    private val moneyValidationUseCase: MoneyValidationUseCase
) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogSpecifyAmountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSpecifyAmountBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {

            amountEditText.setText(productModel.difPrice?:"")

            continueButton.setOnClickListener {
                moneyValidationUseCase.invoke(amountEditText.text.toString()).let {
                    if (it)
                    {
                        communicator.onContinue(amountEditText.text.toString())
                        dismiss()
                    }
                    else
                        amountEditText.error = context.getString(R.string.please_enter_valid_price)
                }

            }

            amountEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    if (charSequence.isNotEmpty()){
                        amountEditText.error = null
                    }
                }
                override fun afterTextChanged(editable: Editable) {}
            })

            cancelButton.setOnClickListener {
                dismiss()
            }

        }

    }

    interface Communicator {
        fun onContinue(amount: String)
    }
}