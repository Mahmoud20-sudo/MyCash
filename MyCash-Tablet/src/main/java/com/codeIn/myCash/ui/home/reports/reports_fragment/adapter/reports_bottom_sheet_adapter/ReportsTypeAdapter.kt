package com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_bottom_sheet_adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.util.isSame
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemReportsTypeBinding
import com.plcoding.reports.data.enums.ReportsTypes

class ReportsTypeAdapter(
    private val selectedReportType : ReportsTypes?,
    private val reportsTypeList: List<ReportsTypes>,
    private val onItemClicked: (ReportsTypes, String) -> Unit
) : RecyclerView.Adapter<ReportsTypeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemReportsTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = reportsTypeList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = reportsTypeList[position]
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemReportsTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
        fun bind(item: ReportsTypes) {
            binding.setUpViews(item)
        }

        private fun ItemReportsTypeBinding.setUpViews(item: ReportsTypes) {
            setTypeViews(item)
            root.setOnClickListener {
                val savedStatedItem = if(selectedReportType != item) item else selectedReportType

                onItemClicked(item, getReportType(savedStatedItem).first)}
            root.setBackgroundColor(
                context.resources.getColor(
                    if (item isSame selectedReportType) R.color.purple else
                        R.color.white, null
                )
            )
        }

        private fun ItemReportsTypeBinding.setTypeViews(item: ReportsTypes) {
            typeLogoIv.isVisible = true
            typeTv.text = getReportType(item).first
            typeLogoIv.setImageDrawable(getReportType(item).second)
        }

        private fun getReportType(item: ReportsTypes) = when (item) {
            ReportsTypes.PRODUCT_INVENTORY_REPORTS ->
                getStringAndDrawableRes(
                    R.string.productInventoryReports, R.drawable.ic_report_layer
                )

            ReportsTypes.MAIN_REPORTS ->
                getStringAndDrawableRes(R.string.mainReports, R.drawable.ic_home_trend_up)

            ReportsTypes.SALES_REPORTS ->
                getStringAndDrawableRes(R.string.salesReports, R.drawable.ic_bag)

            ReportsTypes.PURCHASING_REPORTS ->
                getStringAndDrawableRes(R.string.purchasingReports, R.drawable.ic_box)

            ReportsTypes.EXPENSE_REPORTS ->
                getStringAndDrawableRes(R.string.expenseReports, R.drawable.ic_money_send)

            ReportsTypes.TAX_REPORTS ->
                getStringAndDrawableRes(R.string.taxReports, R.drawable.ic_percentage_circle)
        }

        private fun getStringAndDrawableRes(
            @StringRes value: Int, @DrawableRes drawable: Int
        ): Pair<String, Drawable?> {
            val stringRes = context.getString(value)
            val drawableRes = ContextCompat.getDrawable(context, drawable)
            return Pair(stringRes, drawableRes)
        }
    }
}