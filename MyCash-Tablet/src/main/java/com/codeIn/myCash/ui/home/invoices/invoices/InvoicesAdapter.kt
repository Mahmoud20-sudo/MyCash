package com.codeIn.myCash.ui.home.invoices.invoices

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.data.InvoiceFilter.*
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemInvoiceBinding
import com.codeIn.common.util.gone
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.common.util.visible
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel

class InvoicesAdapter constructor(
    private val context: Context,
    private val communicator: Communicator,
    private val currency : String?
) :
    ListAdapter<InvoiceModel, InvoicesAdapter.ViewHolder>(InvoiceDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem, communicator = communicator, context , currency)

    }


    class ViewHolder(val binding: ItemInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: InvoiceModel,
            communicator: Communicator,
            context: Context,
            currency: String?
        ) {

            binding.apply {
                countTextView.text = "${item.products?.size?:0}"
                dateTextView.text = item.date
                receiptAmountTextView.text = "${item.totalPrice} $currency"
                invoiceNumberTextView.text = "#${item.invoiceNumber}"

                val secondaryColor = ContextCompat.getColor(context, R.color.white)
                val primaryColor = ContextCompat.getColor(context, R.color.secondaryColor)
                val primaryColorBg = ContextCompat.getColor(context, R.color.primaryColor)


                if (item.paymentStatus == PAYMENT_COMPLETED.value.toString()) {
                    invoicePaymentLayout.setBackgroundColor(secondaryColor)
                    paymentCompletedTextView.visible()
                    paymentUncompletedTextView.gone()
                    receiptAmountTextView.changeDrawableAndTextColors(color = primaryColor)
                } else {
                    invoicePaymentLayout.setBackgroundColor(primaryColorBg)
                    paymentCompletedTextView.gone()
                    paymentUncompletedTextView.visible()
                    receiptAmountTextView.changeDrawableAndTextColors(color = secondaryColor)
                }

                val (title, drawableRes) = when (item.invoiceType?.toInt()?:0) {
                    TAX_INVOICE.value -> Pair(R.string.tax_invoice, R.drawable.ic_receipt)
                    SIMPLE_INVOICE.value -> Pair(R.string.simple_invoice, R.drawable.ic_receipt)
                    INSTANT_INVOICE.value -> Pair(R.string.instant_invoices, R.drawable.ic_receipt)
                    PURCHASE_INVOICE.value -> Pair(R.string.purchase_invoice, R.drawable.ic_receipt)
                    PURCHASE_RETURNED.value -> Pair(R.string.purchase_return_invoices, R.drawable.ic_money_change)
                    SALES_RETURNED.value -> Pair(R.string.sales_returned_invoices, R.drawable.ic_money_change)
                    else -> Pair(R.string.invoice, R.drawable.ic_receipt)
                }

                titleTextView.text = context.getString(title)
                titleTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawableRes, 0, 0, 0
                )

                root.setOnClickListener {
                    communicator.onClick(item)
                }
            }

        }
    }

    interface Communicator {
        fun onClick(invoice: InvoiceModel)
    }

}

class InvoiceDiffCallback : DiffUtil.ItemCallback<InvoiceModel>() {
    override fun areItemsTheSame(oldItem: InvoiceModel, newItem: InvoiceModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: InvoiceModel, newItem: InvoiceModel): Boolean {
        return oldItem == newItem
    }
}