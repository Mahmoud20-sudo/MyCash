package com.codeIn.myCash.ui.home.reports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.databinding.ItemSalesReportingBinding
import com.codeIn.myCash.utilities.currencyFormatter
import com.plcoding.reports.data.salesReport.model.beans.SalesReport
import javax.inject.Inject

class SalesReportAdapter @Inject constructor(
    private val prefs: SharedPrefsModule
) :
    RecyclerView.Adapter<SalesReportAdapter.SalesInvoicesViewHolder>() {

    private var dataSet: List<SalesReport> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null
    private lateinit var context: Context

    fun submitInvoices(data: List<SalesReport>) {
        dataSet = data
    }

    fun onItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class SalesInvoicesViewHolder(val binding: ItemSalesReportingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SalesReport) {
            binding.apply {
                startDate.text = item.startDate
                endDate.text = item.endDate

                totalSalesWithoutTax.text = currencyFormatter(
                    item.totalSalesWithOutTax,
                    prefs.getValue(Constants.getCurrency())
                )

                totalSalesByTax.text = currencyFormatter(
                    item.totalSalesWithTax,
                    prefs.getValue(Constants.getCurrency())
                )
                cashPayments.text = currencyFormatter(
                    item.totalCash,
                    prefs.getValue(Constants.getCurrency())
                )
                ePayments.text = currencyFormatter(
                    item.totalVisa,
                    prefs.getValue(Constants.getCurrency())
                )
                remaining.text = currencyFormatter(
                    item.totalRemaining,
                    prefs.getValue(Constants.getCurrency())
                )


                seeDetails.setOnClickListener {
                    onItemClickListener?.onItemClick(item.startDate, item.endDate, bindingAdapterPosition)
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesInvoicesViewHolder {
        context = parent.context
        val binding = ItemSalesReportingBinding.inflate(LayoutInflater.from(context), parent, false)
        return SalesInvoicesViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: SalesInvoicesViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    interface OnItemClickListener {
        fun onItemClick(startDate: String, endDate: String, position: Int)
    }
}