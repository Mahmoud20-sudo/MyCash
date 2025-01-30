package com.codeIn.myCash.ui.home.clients_vendors.cleint_details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.data.PaymentStatus
import com.codeIn.common.data.PaymentType
import com.codeIn.myCash.databinding.ItemReceiptBinding
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptModel

class ReceiptsAdapter constructor(
    private val communicator: Communicator,
    private val currency : String?) :
    ListAdapter<ReceiptModel, ReceiptsAdapter.ViewHolder>(ReceiptDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemReceiptBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(
            item = currentItem, communicator = communicator , currency= currency
        )

    }


    class ViewHolder(val binding: ItemReceiptBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ReceiptModel,
            communicator: Communicator, currency: String?
        ) {

            binding.apply {
                val context = binding.root.context

                receiptTextView.text = "${item.amount} $currency"
                paymentDateTextView.text = item.date
                invoiceNumberTextView.text = "#${item.invoiceData?.invoiceNumber?:0}"
                receiptNumberTextView.text = "#${item.id?:0}"

                val secondaryColor = ContextCompat.getColor(context, R.color.whiteText)
                val primaryColor = ContextCompat.getColor(context, R.color.primaryColor)


                if (item.paymentStatus == PaymentStatus.COMPLETED.value.toString()) {
                    payReceiptTextView.gone()
                    paymentDoneTextView.visible()
                    receiptTextView.changeDrawableAndTextColors(color = primaryColor)
                    receiptLayout.setBackgroundResource(R.drawable.bg_white_s1_main_ripple)
                } else {
                    payReceiptTextView.visible()
                    paymentDoneTextView.gone()
                    payReceiptTextView.setOnClickListener {
                        communicator.payReceipt(item)
                    }
                    receiptTextView.changeDrawableAndTextColors(color = secondaryColor)
                    receiptLayout.setBackgroundResource(R.drawable.bg_primary_s1_main_ripple)

                }

            }

        }
    }

    interface Communicator {
        fun payReceipt(receipt: ReceiptModel)
    }


    class ReceiptDiffCallback : DiffUtil.ItemCallback<ReceiptModel>() {
        override fun areItemsTheSame(oldItem: ReceiptModel, newItem: ReceiptModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReceiptModel, newItem: ReceiptModel): Boolean {
            return oldItem == newItem
        }

    }

}
