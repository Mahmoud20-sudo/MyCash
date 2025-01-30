package com.codeIn.myCash.ui.home.products.add_new_product.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.common.data.Discount
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogAddDiscountBinding


class DiscountOptionsDialog(context: Context, private val discount : Discount, private val listener: Listener) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogAddDiscountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddDiscountBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when (discount){
            Discount.Value -> binding.valueRadioButton.isChecked = true
            Discount.Percentage -> binding.percentageRadioButton.isChecked = true
            Discount.None -> binding.noneRadioButton.isChecked = true
        }

        binding.percentageRadioButton.setOnClickListener {
            if (binding.percentageRadioButton.isChecked && discount != Discount.Percentage )
            {
                listener.onDiscountTypeSelected(Discount.Percentage)
                dismiss()
            }
        }
        binding.valueRadioButton.setOnClickListener {
            if (binding.valueRadioButton.isChecked && discount != Discount.Value )
            {
                listener.onDiscountTypeSelected(Discount.Value)
                dismiss()
            }
        }
        binding.noneRadioButton.setOnClickListener {
            if (binding.noneRadioButton.isChecked && discount != Discount.None )
            {
                listener.onDiscountTypeSelected(Discount.None)
                dismiss()
            }
        }

    }

    interface Listener {
        fun onDiscountTypeSelected(discount: Discount)
    }
}