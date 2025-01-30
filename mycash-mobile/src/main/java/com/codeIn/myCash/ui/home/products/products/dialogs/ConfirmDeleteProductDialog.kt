package com.codeIn.myCash.ui.home.products.products.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogConfirmDeleteProductBinding

class ConfirmDeleteProductDialog(context: Context, private val communicator: Communicator) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogConfirmDeleteProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogConfirmDeleteProductBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.confirmButton.setOnClickListener {
            communicator.deleteProduct()
            dismiss()
        }

    }


    interface Communicator {
        fun deleteProduct()
    }
}