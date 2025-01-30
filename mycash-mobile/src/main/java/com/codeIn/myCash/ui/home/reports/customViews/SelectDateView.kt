package com.codeIn.myCash.ui.home.reports.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.LayoutSelectDateAfterUpdateBinding
import com.codeIn.myCash.utilities.pickers.DatePickerBottomSheet

class SelectDateView(context: Context, attributes: AttributeSet? = null) :
    FrameLayout(context, attributes) {

    private val _binding by lazy {
        LayoutSelectDateAfterUpdateBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var _dateFrom: ((String) -> Unit)? = null
    private var _dateTo: ((String) -> Unit)? = null

    private var isDateFrom: Boolean = false

    init {
        _binding.root
    }

    val binding = _binding

    fun setUpSelectDateViews(fragmentManager: FragmentManager) {
        _binding.apply {
            setOnDateFromClicked(fragmentManager)
            setOnDateToClicked(fragmentManager)
        }
    }


    private fun setOnDateFromClicked(fragmentManager: FragmentManager) =
        binding.dateFromEt.setOnClickListener {
            isDateFrom = true
            dateDialog(fragmentManager)
        }


    private fun setOnDateToClicked(fragmentManager: FragmentManager) =
        binding.dateToEt.setOnClickListener {
            isDateFrom = false

            if (_binding.dateFromEt.text.isNullOrEmpty())
                Toast.makeText(
                    context,
                    R.string.please_select_date_from_first,
                    Toast.LENGTH_SHORT
                ).show()
            else
                dateDialog(fragmentManager)
        }

    private fun dateDialog(fragmentManager: FragmentManager) {
        val datePicker = DatePickerBottomSheet(
            preventFutureDates = false, communicator = getCommunicator()
        )
        datePicker.show(fragmentManager, DatePickerBottomSheet.TAG)
    }

    private fun getCommunicator() = object : DatePickerBottomSheet.Communicator {
        override fun pickDate(date: String) {
            setDateValue(date)
        }
    }

    private fun setDateValue(date: String) {
        if (isDateFrom) {
            _binding.dateFromEt.setText(date)
            _dateFrom?.invoke(date)
        } else {
            _binding.dateToEt.setText(date)
            _dateTo?.invoke(date)
        }

    }


    fun clearDatesInputs() {
        _binding.dateFromEt.setText("")
        _binding.dateToEt.setText("")
    }

    fun getDateFrom(dateFrom: (String) -> Unit) {
        this._dateFrom = dateFrom
    }

    fun getDateTo(dateTo: (String) -> Unit) {
        this._dateTo = dateTo
    }

    fun onClearDatesClicked(onClearDatesClicked: () -> Unit) {
        _binding.clearDates.setOnClickListener {
            clearDatesInputs()
            onClearDatesClicked()
        }
    }

}

