package com.codeIn.myCash.ui.home.invoices.madaTransactions

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
import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionModel

class TransactionAdapter constructor(
    private val context: Context,
    private val currency : String?
) :
    ListAdapter<TransactionModel, TransactionAdapter.ViewHolder>(InvoiceDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem, context , currency)

    }


    class ViewHolder(val binding: ItemInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: TransactionModel,
            context: Context,
            currency: String?
        ) {

            binding.apply {
                countTextView.text = "${item.runRefund}"
                dateTextView.text = item.dateRefund
                invoiceNumberTextView.text = "#${item.id}"
                receiptAmountTextView.text = "${item.amount} $currency"

                product.gone()

                val secondaryColor = ContextCompat.getColor(context, R.color.white)
                val primaryColor = ContextCompat.getColor(context, R.color.secondaryColor)

                invoicePaymentLayout.setBackgroundColor(secondaryColor)
                paymentCompletedTextView.visible()
                paymentUncompletedTextView.gone()
                receiptAmountTextView.changeDrawableAndTextColors(color = primaryColor)


                val (title, drawableRes) = when (item.type?.toInt()?:0) {
                    1 -> Pair(R.string.purchase, R.drawable.ic_transactions)
                    2 -> Pair(R.string.refund_transaction, R.drawable.ic_transactions)
                    else -> Pair(R.string.transactions, R.drawable.ic_receipt)
                }

                titleTextView.text = context.getString(title)
                titleTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawableRes, 0, 0, 0
                )

            }

        }
    }

    interface Communicator {
        fun onClick(invoice: TransactionModel)
    }

}

class InvoiceDiffCallback : DiffUtil.ItemCallback<TransactionModel>() {
    override fun areItemsTheSame(oldItem: TransactionModel, newItem: TransactionModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TransactionModel, newItem: TransactionModel): Boolean {
        return oldItem == newItem
    }
}