package com.codeIn.myCash.ui.options.settings.settings

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.common.data.Languages
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogLanguageOptionsBinding

/**
 * Simple dialog used to show options for the language selection
 * @param context [Context] the context of the activity
 * @param language [Languages] the current language
 * @param communicator [Communicator] the interface used to communicate with the fragment
 */
class LanguageOptionsDialog(context: Context, private val language: Languages, private val communicator: Communicator) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogLanguageOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLanguageOptionsBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when (language){
            Languages.EN -> binding.englishRadioButton.isChecked = true
            Languages.AR -> binding.arabicRadioButton.isChecked = true
        }

    }

    /**
     * Called when the dialog is dismissed, Is used to pass the selected option to the fragment
     */
    override fun dismiss() {
        super.dismiss()
        when{
            binding.englishRadioButton.isChecked  -> communicator.onLanguageSelected(Languages.EN)
            binding.arabicRadioButton.isChecked -> communicator.onLanguageSelected(Languages.AR)
        }
    }

    /**
     * Interface used to communicate with the fragment
     */
    interface Communicator {
        /**
         * Called when the user selects an option from the dialog
         * @param language [Languages] the language selected
         */
        fun onLanguageSelected(language: Languages)
    }
}