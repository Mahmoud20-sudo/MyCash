package com.codeIn.myCash.ui.home.invoices.memorandum

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.databinding.ItemProductDetailsInvoiceBinding
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.ProductModelInMemorandum

class ProductInInvoiceAdapter constructor(
    private val context: Context, ) :
    ListAdapter<ProductModelInMemorandum, ProductInInvoiceAdapter.ViewHolder>(InvoiceDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProductDetailsInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem , context= context)

    }


    class ViewHolder(val binding: ItemProductDetailsInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ProductModelInMemorandum,
            context: Context ,
        ) {

            binding.apply {
                val unitPrice = NumberHelper.persianToEnglishText(item.price?:"0.0").toDouble()
                unitPriceBeforeDiscount.text = unitPrice.toString()
                unitPriceAfterDiscount.text = unitPrice.toString()
                val qty = NumberHelper.persianToEnglishText(item.quantity?:"0.0")
                productName.text = item.product?.name
                quantity.text = item.quantity
                total.text = (unitPrice * qty.toDouble()).toString()
            }

        }
    }

}

class InvoiceDiffCallback : DiffUtil.ItemCallback<ProductModelInMemorandum>() {
    override fun areItemsTheSame(oldItem: ProductModelInMemorandum, newItem: ProductModelInMemorandum): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductModelInMemorandum, newItem: ProductModelInMemorandum): Boolean {
        return oldItem == newItem
    }
}