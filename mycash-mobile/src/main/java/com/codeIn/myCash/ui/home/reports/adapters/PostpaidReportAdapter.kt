package com.codeIn.myCash.ui.home.reports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.databinding.ItemPostpaidReportBinding
import com.codeIn.myCash.utilities.currencyFormatter
import com.plcoding.reports.data.postPaid.model.beans.Postpaid
import com.plcoding.reports.data.returnReport.model.beans.ReturnInvoice
import javax.inject.Inject

class PostpaidReportAdapter @Inject constructor(
    private val prefs: SharedPrefsModule
) : RecyclerView.Adapter<PostpaidReportAdapter.SalesInvoicesViewHolder>() {

    private var dataSet: List<Postpaid> = emptyList()
    private var onItemClickListener: OnPostPaidReportClickListener? = null
    private lateinit var context: Context

    fun submitPostpaidReport(data: List<Postpaid>) {
        dataSet = data
    }

    fun onPostPaidReportClickListener(listener: OnPostPaidReportClickListener) {
        onItemClickListener = listener
    }

    inner class SalesInvoicesViewHolder(val binding: ItemPostpaidReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Postpaid) {
            binding.apply {
                date.text = item.dateOfPayment
                recepitNum.text = item.receiptNumber.toString()
                invoiceNum.text = item.invoiceNumber.toString()
                postpaidBranch.text = item.branchName
                receiptStatus.text = item.receiptState
                totalInvoice.text = currencyFormatter(
                    item.totalInvoice,
                    prefs.getValue(Constants.getCurrency())
                )
                receiptValue.text = currencyFormatter(
                    item.receiptAmount,
                    prefs.getValue(Constants.getCurrency())
                )

                seeReceipt.setOnClickListener {
                    onItemClickListener?.onPostPaidReportClick(item.receiptId)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesInvoicesViewHolder {
        context = parent.context
        val binding = ItemPostpaidReportBinding.inflate(LayoutInflater.from(context), parent, false)
        return SalesInvoicesViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: SalesInvoicesViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    interface OnPostPaidReportClickListener {
        fun onPostPaidReportClick(receiptId: Int)
    }
}