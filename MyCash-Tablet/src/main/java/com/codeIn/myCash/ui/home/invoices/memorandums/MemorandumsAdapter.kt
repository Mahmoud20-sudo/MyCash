package com.codeIn.myCash.ui.home.invoices.memorandums

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.data.NoteType
import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemInvoiceBinding
import com.codeIn.common.util.gone
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.common.util.visible
import com.codeIn.myCash.ui.home.invoices.CreditorDebtorNote
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumModel

class MemorandumsAdapter constructor(
    private val context: Context,
    private val communicator: Communicator,
    private val currency : String?
) : ListAdapter<MemorandumModel, MemorandumsAdapter.ViewHolder>(CreditorDebtorNoteDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem, communicator = communicator, context= context , currency= currency)

    }


    class ViewHolder(val binding: ItemInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: MemorandumModel,
            communicator: Communicator,
            context: Context,
            currency: String?
        ) {

            binding.apply {
                countTextView.text = "x${item.products?.size}"
                dateTextView.text = item.date?:"----"
                receiptAmountTextView.text ="${item.totalPrice} $currency"
                invoiceNumberTextView.text = "#${item.id}"

                val secondaryColor = ContextCompat.getColor(context, R.color.white)
                val primaryColor = ContextCompat.getColor(context, R.color.secondaryColor)

                invoicePaymentLayout.setBackgroundColor(secondaryColor)
                paymentCompletedTextView.visible()
                paymentUncompletedTextView.gone()
                receiptAmountTextView.changeDrawableAndTextColors(color = primaryColor)

                val (title, drawableRes) = when (item.type) {
                    NoteType.CREDITOR.value -> Pair(R.string.creditor_note, R.drawable.ic_receipt)
                    NoteType.DEBTOR.value -> Pair(R.string.debtor_note, R.drawable.ic_receipt)
                    else -> Pair(R.string.creditor_and_debtor_notes, R.drawable.ic_receipt)
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
        fun onClick(note: MemorandumModel)
    }

}

class CreditorDebtorNoteDiffCallback : DiffUtil.ItemCallback<MemorandumModel>() {
    override fun areItemsTheSame(oldItem: MemorandumModel, newItem: MemorandumModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MemorandumModel, newItem: MemorandumModel): Boolean {
        return oldItem == newItem
    }
}