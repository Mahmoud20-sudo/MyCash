package com.codeIn.myCash.utilities.dialogs

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
class LanguageOptionsDialog(
    context: Context,
    currentLanguage: String?,
    private val communicator: Communicator
) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogLanguageOptionsBinding
    private var language: Languages = if (currentLanguage == "en")
        Languages.EN
    else
        Languages.AR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLanguageOptionsBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setSelectedLanguage()

        binding.languagesRg.setOnCheckedChangeListener { _, _ ->
            dismiss()
        }

        when (language) {
            Languages.EN -> binding.englishRadioButton.isChecked = true
            Languages.AR -> binding.arabicRadioButton.isChecked = true
        }

    }

    private fun setSelectedLanguage() {
        when (language) {
            Languages.EN -> binding.englishRadioButton.isChecked = true
            Languages.AR -> binding.arabicRadioButton.isChecked = true
        }
    }

    /**
     * Called when the dialog is dismissed, Is used to pass the selected option to the fragment
     */
    override fun dismiss() {
        super.dismiss()
        when (language) {
            Languages.EN -> if (binding.arabicRadioButton.isChecked) communicator.onLanguageSelected(Languages.AR)
            Languages.AR -> if (binding.englishRadioButton.isChecked) communicator.onLanguageSelected(Languages.EN)
        }
    }

    fun showLangDialog() {
        if (isShowing) dismiss()
        show()
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