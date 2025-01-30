package com.codeIn.myCash.utilities.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.common.util.gone
import com.codeIn.common.util.invisible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogInfoBinding

private class InfoDialogImpl(
    context: Context,
    val title: String? = null,
    val message: String? = null,
    val positiveButtonText: String? = null,
    val negativeButtonText: String? = null,
    private val onNegativeButtonClick: (() -> Unit)? = null,
    private val onPositiveButtonClick: (() -> Unit)? = null
) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogInfoBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        title?.let {
            binding.titleTextView.text = it
        } ?: run {
            binding.titleTextView.invisible()
        }

        message?.let {
            binding.descTextView.text = it
        } ?: run {
            binding.descTextView.invisible()
        }

        positiveButtonText?.let {
            binding.confirmButton.text = it
        } ?: run {
            binding.confirmButton.gone()
        }

        negativeButtonText?.let {
            binding.cancelButton.text = it
        } ?: run {
            binding.cancelButton.gone()
        }

        binding.cancelButton.setOnClickListener {
            onNegativeButtonClick?.invoke()
            dismiss()
        }

        binding.confirmButton.setOnClickListener {
            onPositiveButtonClick?.invoke()
            dismiss()
        }
    }
}

class InfoDialog {
    private var dialogImpl: InfoDialogImpl? = null
    fun show(
        context: Context,
        title: String? = null,
        message: String? = null,
        positiveButtonText: String? = null,
        negativeButtonText: String? = null,
        onNegativeButtonClick: (() -> Unit)? = null,
        onPositiveButtonClick: (() -> Unit)? = null
    ) {
        dialogImpl?.dismiss()
        dialogImpl = InfoDialogImpl(
            context = context,
            title = title,
            message = message,
            positiveButtonText = positiveButtonText,
            negativeButtonText = negativeButtonText,
            onNegativeButtonClick = onNegativeButtonClick,
            onPositiveButtonClick = onPositiveButtonClick
        )
        dialogImpl?.setCanceledOnTouchOutside(false)
        dialogImpl?.show()
    }

    fun dismiss() {
        dialogImpl?.dismiss()
    }

}