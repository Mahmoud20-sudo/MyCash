package com.codeIn.myCash.ui.home.reports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemSalesInvoiceBinding
import com.codeIn.myCash.utilities.currencyFormatter
import com.plcoding.reports.data.salesorbuy.model.SalesOrBuyModel
import javax.inject.Inject

class SalesInvoicesAdapter @Inject constructor(
    private val prefs: SharedPrefsModule
) :
    RecyclerView.Adapter<SalesInvoicesAdapter.SalesInvoicesViewHolder>() {

    private var dataSet: List<SalesOrBuyModel> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null
    private lateinit var context: Context

    fun submitInvoices(data: List<SalesOrBuyModel>) {
        dataSet = data
    }

    fun onItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class SalesInvoicesViewHolder(val binding: ItemSalesInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SalesOrBuyModel) {
            binding.apply {
                date.text = item.date
                invoiceNumber.text = item.invoiceNumber.toString()

                // Set invoice type text and icon
                totalSalesByTax.apply {
                    val (invoiceTextResId, iconResId) = when (item.invoiceType) {
                        1 -> R.string.quickTaxInvoicePDF to R.drawable.ic_simplified_tax_invoice
                        else -> R.string.tax_invoice to R.drawable.ic_tax_invoice
                    }
                    text = context.getString(invoiceTextResId)
//                    setCompoundDrawablesWithIntrinsicBounds(0, 0, iconResId, 0)
                }

                // Set payment type text
                paymentType.text = when (item.paymentType) {
                    1 -> context.getString(R.string.cash)
                    2 -> context.getString(R.string.visa)
                    3 -> context.getString(R.string.postpaid)
                    4 -> context.getString(R.string.cash_and_credit_card)
                    else -> context.getString(R.string.unknown)
                }

                // Format total sale with currency
                totalSale.text = currencyFormatter(
                    item.totalPrice.toDouble(),
                    prefs.getValue(Constants.getCurrency())
                )

                // Set click listener for viewing invoice
                seeInvoice.setOnClickListener {
                    onItemClickListener?.onItemClick(item.id, bindingAdapterPosition)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesInvoicesViewHolder {
        context = parent.context
        val binding = ItemSalesInvoiceBinding.inflate(LayoutInflater.from(context), parent, false)
        return SalesInvoicesViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: SalesInvoicesViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    interface OnItemClickListener {
        fun onItemClick(invoiceNumber: Int, position: Int)
    }
}