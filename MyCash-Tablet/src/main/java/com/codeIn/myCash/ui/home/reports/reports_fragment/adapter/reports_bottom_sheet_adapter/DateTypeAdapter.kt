package com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_bottom_sheet_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.util.isSame
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemReportsTypeBinding
import com.plcoding.reports.data.enums.DateType
import com.plcoding.reports.data.enums.DateType.DAY
import com.plcoding.reports.data.enums.DateType.MONTH
import com.plcoding.reports.data.enums.DateType.WEEK
import com.plcoding.reports.data.enums.DateType.YEAR

class DateTypeAdapter(
    private val selectedDateType: Int?,
    private val dateTypeList: List<DateType>,
    private val onItemClicked: (DateType, String) -> Unit
) : RecyclerView.Adapter<DateTypeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemReportsTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = dateTypeList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dateTypeList[position]
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemReportsTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
        fun bind(item: DateType) {
            binding.setUpViews(item)
        }

        private fun ItemReportsTypeBinding.setUpViews(item: DateType) {
            root.setOnClickListener {
                val selectedItem = if (item.value isSame selectedDateType) DateType.NONE else item
                onItemClicked(selectedItem, getDateType(selectedItem))
            }
            typeTv.text = getDateType(item)
            root.setBackgroundColor(
                context.resources.getColor(
                    if (item.value isSame selectedDateType) R.color.purple else
                        R.color.white, null
                )
            )
        }

        private fun getDateType(item: DateType) = when (item) {
            DAY -> getStringRes(R.string.today)
            WEEK -> getStringRes(R.string.weekly)
            MONTH -> getStringRes(R.string.monthly)
            YEAR -> getStringRes(R.string.annually)
            DateType.NONE -> getStringRes(R.string.select_date_type)
        }
        private fun getStringRes(@StringRes value: Int) = context.getString(value)
    }
}