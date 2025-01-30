package com.codeIn.myCash.ui.home.invoices.invoice

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.data.Discount
import com.codeIn.common.data.InvoiceFilter.*
import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.R
import com.codeIn.common.util.gone
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.common.util.visible
import com.codeIn.myCash.databinding.ItemProductDetailsInvoiceBinding
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import kotlin.math.abs

class ProductInInvoiceAdapter constructor(
    private val context: Context, ) :
    ListAdapter<ProductInInvoiceModel, ProductInInvoiceAdapter.ViewHolder>(InvoiceDiffCallback()) {


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
            item: ProductInInvoiceModel,
            context: Context ,
        ) {

            binding.apply {
                var unitPrice = NumberHelper.persianToEnglishText(item.unitPrice?:"0.0").toDouble()
                unitPriceBeforeDiscount.text = abs(unitPrice).toString()
                unitPriceAfterDiscount.text = abs(unitPrice).toString()
                val hasDiscount = NumberHelper.persianToEnglishText(
                    item.discountType?:"0").toDouble().toInt()
                val hasDiscountInvoice = NumberHelper.persianToEnglishText(
                    item.invoiceDiscountType?:"0").toDouble().toInt()

                if (hasDiscount != Discount.None.value || hasDiscountInvoice != Discount.None.value ){
                    unitPrice -= NumberHelper.persianToEnglishText(item.unitDiscountPrice ?: "0.0").toDouble()
                    unitPriceAfterDiscount.text = abs(unitPrice).toString()
                    unitPriceBeforeDiscount.visible()
                    unitPriceBeforeDiscount.paintFlags = binding.unitPriceAfterDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }

                val qty = NumberHelper.persianToEnglishText(item.quantity?:"0.0")
                productName.text = item.product?.name
                quantity.text = item.quantity
                total.text = abs(unitPrice * qty.toDouble()).toString()
            }

        }
    }

}

class InvoiceDiffCallback : DiffUtil.ItemCallback<ProductInInvoiceModel>() {
    override fun areItemsTheSame(oldItem: ProductInInvoiceModel, newItem: ProductInInvoiceModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductInInvoiceModel, newItem: ProductInInvoiceModel): Boolean {
        return oldItem == newItem
    }
}