package com.codeIn.myCash.ui.home.products.products.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogProductOptionsBinding


class ProductOptionsDialog(context: Context, private val communicator: Communicator) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogProductOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogProductOptionsBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)



        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.editProductTextView.setOnClickListener {
            communicator.editProduct()
            dismiss()
        }

        binding.deleteProductTextView.setOnClickListener {
            communicator.deleteProduct()
            dismiss()
        }

    }


    interface Communicator {
        fun editProduct()
        fun deleteProduct()
    }
}