package com.codeIn.myCash.ui.home.clients_vendors.cleint_details.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemPaymentBinding
import com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.Payment
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel

class PaymentsAdapter constructor(
    private val context: Context,
    private val communicator: Communicator,
    private val currency : String?) :
    ListAdapter<InvoiceModel, PaymentsAdapter.ViewHolder>(PaymentDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem, communicator = communicator,context , currency)

    }


    class ViewHolder(val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: InvoiceModel,
            communicator: Communicator,
            context: Context,
            currency : String?
        ) {

            binding.apply {
                countTextView.text = "x${item.products?.size}"
                dateTextView.text = item.date
                receiptAmountTextView.text = "${item.totalPrice} $currency"
                receiptNumberTextView.text = "#${item.invoiceNumber}"

                val secondaryColor = ContextCompat.getColor(context, R.color.mainBlack)
                val primaryColor = ContextCompat.getColor(context, R.color.primaryColor)

                if (item.paymentStatus == InvoiceFilter.PAYMENT_COMPLETED.value.toString()) {
                    receiptTextView.text = context.getString(R.string.payment_completed)
                    receiptTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_tick_circle_small,0,0,0)
                    receiptTextView.changeDrawableAndTextColors(
                        color = primaryColor
                    )
                    receiptAmountTextView.changeDrawableAndTextColors(color = primaryColor)
                } else {
                    receiptTextView.text = context.getString(R.string.payment_uncompleted)
                    receiptTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
                    receiptTextView.changeDrawableAndTextColors(
                        color = secondaryColor
                    )
                    receiptAmountTextView.changeDrawableAndTextColors(color = secondaryColor)
                }

            }

        }
    }

    interface Communicator {
        fun payReceipt(payment: Payment)
    }

}

class PaymentDiffCallback : DiffUtil.ItemCallback<InvoiceModel>() {
    override fun areItemsTheSame(oldItem: InvoiceModel, newItem: InvoiceModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: InvoiceModel, newItem: InvoiceModel): Boolean {
        return oldItem == newItem
    }

}