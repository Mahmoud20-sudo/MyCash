package com.codeIn.myCash.ui.options.settings.invoice_settings.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogStartCurrentPurchaseInvoiceOrderBinding
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User

class StartPurchaseInvoiceOrderNoDialog (
    context: Context,
    private val listener: OrderNoListener
)  : Dialog(context, R.style.PauseDialog){

    private lateinit var binding: DialogStartCurrentPurchaseInvoiceOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogStartCurrentPurchaseInvoiceOrderBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.apply {
            startButton.setOnClickListener {
                listener.startPurchaseInvoiceOrder(startPurchaseInvoiceOrderNoEditText.text.toString())
            }
        }

        setCanceledOnTouchOutside(false)
    }



}