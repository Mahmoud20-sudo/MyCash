package com.codeIn.myCash.ui.home.reports.reports_fragment.custom_views

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.LayoutSelectDateBinding
import com.codeIn.myCash.utilities.pickers.DatePickerBottomSheet

class SelectDateView(context: Context, attributes: AttributeSet? = null) :
    FrameLayout(context, attributes) {

    private val _binding by lazy {
        LayoutSelectDateBinding.inflate(LayoutInflater.from(context), this, true)
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
            handleDateFromAfterTextChanged()
            handleDateToAfterTextChanged()
        }
    }


    fun setOnDateFromClicked(fragmentManager: FragmentManager) =
        binding.dateFromEt.setOnClickListener {
            isDateFrom = true
            dateDialog(fragmentManager)
        }


    fun setOnDateToClicked(fragmentManager: FragmentManager) =
        binding.dateToEt.setOnClickListener {
            isDateFrom = false
            dateDialog(fragmentManager)
        }

    private fun LayoutSelectDateBinding.handleDateFromAfterTextChanged() =
        dateFromEt.doAfterTextChanged {
            val backGroundRes = it?.getBackGroundColor() ?: 0
            dateFromEt.setBackgroundResource(backGroundRes)
            dateToEt.isEnabled = !it.isNullOrEmpty()
        }


    private fun LayoutSelectDateBinding.handleDateToAfterTextChanged() =
        dateToEt.doAfterTextChanged {
            val backGroundRes = it?.getBackGroundColor() ?: 0
            dateToEt.setBackgroundResource(backGroundRes)
        }


    private fun Editable.getBackGroundColor() = if (this.isEmpty()) {
        R.drawable.bg_main_balck40_r50_s1
    } else {
        R.drawable.bg_main_black_r50_s1
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
            return
        }
        _binding.dateToEt.setText(date)
        _dateTo?.invoke(date)
    }


    private fun resetDateForm() = _binding.dateFromEt.setText("")


    private fun restDateTo() = _binding.dateToEt.setText("")

    fun clearDatesInputs(){
        _binding.dateFromEt.setText("")
        _binding.dateToEt.setText("")
    }

    fun getDateFrom(dateFrom: (String) -> Unit) {
        this._dateFrom = dateFrom
    }

    fun getDateTo(dateTo: (String) -> Unit) {
        this._dateTo = dateTo
    }

    fun setOnFilterIvClicked(onFilterClicked: () -> Unit) =
        _binding.filterIv.setOnClickListener { onFilterClicked() }

    fun showFilter(isVisible : Boolean) {binding.filterIv.isVisible = isVisible}

}