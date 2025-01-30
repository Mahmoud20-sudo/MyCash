package com.codeIn.myCash.utilities.pickers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.BottomSheetDatePickerBinding
import com.codeIn.myCash.utilities.pickers.DatePickerBottomSheet.Communicator
import com.codeIn.common.util.toEditable
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import java.text.SimpleDateFormat
import java.util.Locale


/**
 *  * A DatePickerBottomSheet is a modal bottom sheet that displays a date picker.
 *  * It may take a title and a minimum date as parameters.
 *  * It returns the selected date as a string.  Caller must implement the [Communicator] interface.
 *
 * @param title the title of the bottom sheet
 * @param minDate the minimum date that can be selected
 * @param maxDate the maximum date that can be selected
 * @param preventFutureDates if true, the user will not be able to select a future date
 * @param communicator the communicator interface to send the selected date to the caller
 */
class DatePickerBottomSheet(
    private val title: String = "",
    private val minDate: String = "",
    private val maxDate: String = "",
    private val preventFutureDates: Boolean = true,
    private val communicator: Communicator ,
    private val preventOldBates : Boolean = false
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDatePickerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDatePickerBinding.inflate(layoutInflater)
        (dialog as? BottomSheetDialog)?.behavior?.state = STATE_EXPANDED

        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onResume() {
        super.onResume()

        binding.backButton.setOnClickListener {
            dismiss()
        }

        // Set title if the caller passed one
        if (title != "")
            binding.pickDateTextView.text = title


        // Set min date if the caller passed one
        if (minDate != "") {
            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val date = formatter.parse(minDate)
            if (date?.time != null)
                binding.datePicker.minDate = date.time
        }

        // Set max date if the caller passed one
        if (maxDate != "") {
            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val date = formatter.parse(maxDate)
            if (date?.time != null)
                binding.datePicker.maxDate = date.time
        }

        // Set max date to yesterday to prevent the user from selecting a future date
        if (preventFutureDates)
            binding.datePicker.maxDate = System.currentTimeMillis()

        if (preventOldBates){
            binding.datePicker.minDate = System.currentTimeMillis()
        }

        binding.confirmButton.setOnClickListener {

            // Format the selected date
            val date: String
            binding.datePicker.apply {
                date = if (month<9)
                    "${this.year}-0${this.month + 1}-${dayOfMonth}"
                else
                    "${this.year}-${this.month + 1}-${dayOfMonth}"
            }

            // Send the selected date to the caller
            communicator.pickDate(date)
            dismiss()
        }

    }


    /**
     * Communicator interface to send the selected date to the caller
     */
    interface Communicator {
        /**
         * @param date the selected date in the format "yyyy-MM-dd"
         */
        fun pickDate(date: String)
    }

}


/**
 * This utility class is used to show a date picker in a bottom sheet.
 */
class DatePicker {
    // keep a reference to the DatePickerBottomSheet to dismiss it if it's already showing
    private val datePicker: DatePickerBottomSheet? = null
    fun showDatePicker(
        childFragmentManager: FragmentManager,
        editText: EditText? =null,
        textView: TextView? = null,
        searchView: SearchView? = null,
        onDatePicked: ((String) -> Unit)? = null,
        preventFutureDates: Boolean = false,
        preventOldBates: Boolean = false ,
    ) {
        datePicker?.dismiss()
        val datePicker = DatePickerBottomSheet(
            preventFutureDates = preventFutureDates,
            preventOldBates = preventOldBates,
            communicator = object : Communicator {
                override fun pickDate(date: String) {
                    editText?.text = date.toEditable()
                    textView?.text = date
                    searchView?.setQuery(date , false )
                    onDatePicked?.invoke(date)
                }
            })
        datePicker.show(childFragmentManager, DatePickerBottomSheet.TAG)
    }
}