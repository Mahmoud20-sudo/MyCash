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
import com.google.android.material.snackbar.Snackbar

class EmailDialog(
    context: Context,
    private val communicator: Communicator,
    private val validation: Validation
) :
    Dialog(context, R.style.PauseDialog) {
    private lateinit var binding: DialogEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogEmailBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.continueButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()

            validation.validate(email = email)
                .let {
                    if (it != InvalidInput.NONE) {
                        Snackbar.make(
                            binding.root,
                            context.getString(R.string.email_error_message),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        communicator.onContinueClicked(email)
                        dismiss()
                    }
                }
        }
    }

    interface Communicator {
        fun onContinueClicked(email: String)
    }
}