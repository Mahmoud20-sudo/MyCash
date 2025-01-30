package com.codeIn.myCash.ui.options.profile.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.util.Validation
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogEmailBinding
import com.codeIn.myCash.databinding.DialogPhoneBinding
import com.google.android.material.snackbar.Snackbar

class PhoneDialog(
    context: Context,
    private val communicator: Communicator,
    private val validation: Validation
) :
    Dialog(context, R.style.PauseDialog) {
    private lateinit var binding: DialogPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPhoneBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.continueButton.setOnClickListener {
            val phone = binding.phoneLayout.phoneNumberEditText.text.toString()

            validation.checkPhone(phone)
                .let {
                    if (it != InvalidInput.NONE)
                        Snackbar.make(
                            binding.root,
                            context.getString(R.string.please_enter_a_valid_saudi_phone_number),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    else {
                        communicator.onContinueClicked(phone)
                        dismiss()
                    }
                }
        }
    }

    interface Communicator {
        fun onContinueClicked(phone: String)
    }
}