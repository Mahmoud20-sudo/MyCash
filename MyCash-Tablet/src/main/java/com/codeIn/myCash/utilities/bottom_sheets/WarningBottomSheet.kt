package com.codeIn.myCash.utilities.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.BottomSheetWarningBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.security.PrivateKey


/**
 *  A bottom sheet dialog fragment to show a warning message to the user. It has a title, a message and two buttons, one to confirm and one to cancel.
 *  @param title [String] the title of the bottom sheet.
 *  @param message [String] the message of the bottom sheet.
 *  @param confirmText [String] the text of the confirm button, if null the button will display the default text "Confirm".
 *  @param cancelText [String] the text of the cancel button, if null the button will display the default text "Cancel".
 *  @param communicator [Communicator] the communicator interface to send the confirmation or cancellation of the bottom sheet to the caller.
 *  @see Communicator
 */
class WarningBottomSheet constructor(
    private val title: String,
    private val message: String,
    private val confirmText: String? = null,
    private val cancelText: String? = null,
    private val communicator: Communicator,
    private val cancelable : Boolean = true
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetWarningBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetWarningBinding.inflate(layoutInflater)
        (dialog as? BottomSheetDialog)?.behavior?.state = STATE_EXPANDED

        dialog?.setCancelable(cancelable)
        dialog?.setCanceledOnTouchOutside(cancelable)


        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onResume() {
        super.onResume()

        binding.backButton.setOnClickListener {
            if (cancelable)
                dismiss()
        }

        //set the title and the message of the bottom sheet
        binding.titleTextView.text = title
        binding.messageTextView.text = message
        if ( confirmText != null )
            binding.confirmButton.text = confirmText
        if ( cancelText != null )
            binding.cancelButton.text = cancelText
        //set the click listener for the confirm button
        binding.confirmButton.setOnClickListener {
            communicator.confirm()
            if (cancelable)
                dismiss()
        }

        //set the click listener for the cancel button
        binding.cancelButton.setOnClickListener {
            communicator.cancel()
            if (cancelable)
                dismiss()
        }

    }

    /**
     * Communicator interface to send the confirmation or cancellation of the bottom sheet to the caller.
     */
    interface Communicator {
        /**
         * function to send the confirmation of the bottom sheet to the caller.
         * @return [Unit]
         */
        fun confirm()

        /**
         * function to send the cancellation of the bottom sheet to the caller.
         * @return [Unit]
         */
        fun cancel() {}
    }

}
