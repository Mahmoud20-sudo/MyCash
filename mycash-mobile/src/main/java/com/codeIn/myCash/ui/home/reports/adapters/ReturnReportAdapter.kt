package com.codeIn.myCash.ui.home.reports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemReturnReportBinding
import com.codeIn.myCash.utilities.currencyFormatter
import com.plcoding.reports.data.returnReport.model.beans.ReturnInvoice
import javax.inject.Inject

class ReturnReportAdapter @Inject constructor(
    private val prefs: SharedPrefsModule
) :
    RecyclerView.Adapter<ReturnReportAdapter.SalesInvoicesViewHolder>() {

    private var dataSet: List<ReturnInvoice> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null
    private lateinit var context: Context

    fun submitInvoices(data: List<ReturnInvoice>) {
        dataSet = data
    }

    fun onItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class SalesInvoicesViewHolder(val binding: ItemReturnReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReturnInvoice) {
            binding.apply {
                date.text = item.date
                dateOfReturn.text = item.dateRefund

                val (title, drawableRes) = item.run {
                    val title = if(invoiceType == 2) R.string.tax_invoice else R.string.simplified_tax_invoice
                    val resId = if(isReturn?.toInt() == 1) R.drawable.ic_returned else 0
//                    if(type?.toInt() == 2) Pair(title, R.drawable.ic_instant)
                    Pair(title, if(type?.toInt() == 2) R.drawable.ic_instant else resId)
                }
                invoiceType.text = context.getString(title)


                paymentType.text = getPaymentType(item.paymentType)

                totalBill.text = currencyFormatter(
                    item.totalPrice,
                    prefs.getValue(Constants.getCurrency())
                )
                returnedPrice.text = currencyFormatter(
                    item.returnedPrice,
                    prefs.getValue(Constants.getCurrency())
                )

                seeInvoice.setOnClickListener {
                    onItemClickListener?.onReturnReportClick(item.id, bindingAdapterPosition)
                }
            }

        }

        private fun getPaymentType(paymentType: Int) = when (paymentType) {
                1 -> context.getString(R.string.cash)
                2 -> context.getString(R.string.credit)
                3 -> context.getString(R.string.postpaid)
                4 -> context.getString(R.string.cash_and_credit_card)
                5 -> context.getString(R.string.cash_and_credit_card)
                6 -> context.getString(R.string.installment)
                else -> context.getString(R.string.installment)

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesInvoicesViewHolder {
        context = parent.context
        val binding = ItemReturnReportBinding.inflate(LayoutInflater.from(context), parent, false)
        return SalesInvoicesViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: SalesInvoicesViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    interface OnItemClickListener {
        fun onReturnReportClick(invoiceNumber: String, position: Int)
    }
}