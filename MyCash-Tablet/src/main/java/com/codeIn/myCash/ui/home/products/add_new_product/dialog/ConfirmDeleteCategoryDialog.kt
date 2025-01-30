package com.codeIn.myCash.ui.home.products.add_new_product.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogConfirmDeleteProductBinding

class ConfirmDeleteCategoryDialog(context: Context, private val listener: Listener, private val position: Int) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogConfirmDeleteProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogConfirmDeleteProductBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            header.text = context.resources.getString(R.string.delete_category)
            content.text = content.resources.getString(R.string.hint_delete_category)
            cancelButton.setOnClickListener {
                dismiss()
            }

            confirmButton.setOnClickListener {
                listener.deleteCategories(position)
                dismiss()
            }
        }
    }


    interface Listener {
        fun deleteCategories(position : Int)
    }
}