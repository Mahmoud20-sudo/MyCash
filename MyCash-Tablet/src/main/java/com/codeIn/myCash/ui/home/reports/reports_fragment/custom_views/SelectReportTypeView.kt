package com.codeIn.myCash.ui.home.reports.reports_fragment.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.LayoutSelectReportTypeBinding
import com.plcoding.reports.data.enums.DateType
import java.time.Month
import java.time.Year

class SelectReportTypeView(context: Context, attributes: AttributeSet? = null) :
    FrameLayout(context, attributes) {

    private val binding by lazy {
        LayoutSelectReportTypeBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        binding.root
    }


    fun setOnReportFilterTvClicked(onReportFilterClicked: () -> Unit) =
        binding.reportFilterTv.setOnClickListener {
            onReportFilterClicked()
        }

    fun setReportFilterTv(value: CharSequence) {
        binding.reportFilterTv.text = value
    }

    fun setOnDateTypeFilterTvClicked(onDayFilterClicked: () -> Unit) =
        binding.dateTypeFilterTv.setOnClickListener {
            onDayFilterClicked()
        }

    fun setDateTypeFilterTv(value: DateType) {
        val title = when (value) {
            DateType.YEAR -> context.getString(R.string.annually)
            DateType.MONTH -> context.getString(R.string.monthly)
            DateType.WEEK -> context.getString(R.string.weekly)
            DateType.DAY -> context.getString(R.string.today)
            else -> context.getString(R.string.select_date_type)
        }
        binding.dateTypeFilterTv.text = title
    }

    fun restDateType() {
        binding.dateTypeFilterTv.text = context.getString(R.string.select_date_type)
    }
}