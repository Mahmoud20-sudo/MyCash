package com.codeIn.myCash.ui.home.reports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemReportTypeBinding
import com.plcoding.reports.data.enums.ReportsFilterTypes
import io.nearpay.sdk.utils.textColor
import timber.log.Timber

class TopLevelReportsFiltersAdapter(
    private val reportsTypeList: List<ReportsFilterTypes>,
) : RecyclerView.Adapter<TopLevelReportsFiltersAdapter.ViewHolder>() {

    private var topLevelReportsListener: TopLevelReportsListener? = null
    private var selectedReportType = 0

    fun setTopLevelReportsListener(topLevelReportsListener: TopLevelReportsListener) {
        this.topLevelReportsListener = topLevelReportsListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemReportTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount() = reportsTypeList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(reportsTypeList[position], selectedReportType == position)
    }

    fun submitSelectedItem(selectedReportPosition: Int) {
        this.selectedReportType = selectedReportPosition
        Timber.tag("submitSelectedItem").d("submitSelectedItem called $selectedReportPosition")
    }

    inner class ViewHolder(private val binding: ItemReportTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context

        fun bind(item: ReportsFilterTypes, isSelected: Boolean) {
            binding.apply {
                filterType.text = context.getString(item.filterName)
                filterType.setOnClickListener(::onFilterClicked)

                if (isSelected) {
                    filterType.setBackgroundResource(R.drawable.bg_primary500_r30)
                    filterType.setCompoundDrawablesWithIntrinsicBounds(0, 0, item.selectedIcon, 0)
                    filterType.textColor(context, R.color.white)
                    topLevelReportsListener?.onItemClicked(reportsTypeList[selectedReportType])
                } else {
                    filterType.setBackgroundResource(R.drawable.bg_stroke_neutral500_r30)
                    filterType.setCompoundDrawablesWithIntrinsicBounds(0, 0, item.unselectedIcon, 0)
                    filterType.textColor(context, R.color.neutral500)
                }
            }
        }

        private fun onFilterClicked(view: View?) {
            if (selectedReportType == bindingAdapterPosition) return

            val previousSelected = selectedReportType
            selectedReportType = bindingAdapterPosition

            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedReportType)
        }
    }

    interface TopLevelReportsListener {
        fun onItemClicked(reportsFilterTypes: ReportsFilterTypes)
    }
}