package com.codeIn.myCash.utilities.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogContactUsBinding


private class ContactUsDialogImpl(
    context: Context,
    val qrCode: String? = null,
    val whatsapp: String? = null,
) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogContactUsBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        Glide.with(context)
            .load(whatsapp)
            .placeholder(R.drawable.ic_settings_qrcode)
            .error(R.drawable.ic_settings_qrcode)
            .into(binding.qrImageView)

        binding.confirmButton.setOnClickListener {
            openWhatsApp()
        }
    }

    fun openWhatsApp() {
        val packageManager = context.packageManager
        val whatsappIntent = Intent(Intent.ACTION_VIEW)
        whatsappIntent.data = Uri.parse("https://wa.me/$whatsapp?text=Hello, I want to ask about your services")
        if (whatsappIntent.resolveActivity(packageManager) != null) {
            context.startActivity(whatsappIntent)
        } else {
            Toast.makeText(context, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show()
        }
    }
}

class ContactUsDialog {
    private var dialogImpl: ContactUsDialogImpl? = null
    fun show(
        context: Context,
        qrCode: String,
        whatsapp: String,
    ) {
        dialogImpl?.dismiss()
        dialogImpl = ContactUsDialogImpl(
            context = context,
            qrCode = qrCode,
            whatsapp = whatsapp,
        )
        dialogImpl?.setCanceledOnTouchOutside(true)
        dialogImpl?.show()
    }

}