package com.codeIn.myCash.utilities.pdf

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.databinding.ItemInvoicePdfBinding
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel

class ProductInPDFAdapter constructor(
    private val context: Context,
    private var showDesc: Boolean = false , 
    private var showTax: Boolean = false , 
) :
    ListAdapter<ProductInInvoiceModel, ProductInPDFAdapter.ViewHolder>(ProductDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInvoicePdfBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(item = currentItem, context, showDesc , showTax)
    }


    class ViewHolder(private val binding: ItemInvoicePdfBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ProductInInvoiceModel,
            context: Context,
            showDesc : Boolean , 
            showTax : Boolean
        ) {

            binding.apply {
                if (showTax)
                {
                    if (item.product?.taxAvailable == "1")
                    {
//                        binding.tax.text = item.tax+"%"
                        binding.taxPrice.text = item.taxPrice
                    }
                    else
                    {
                        binding.tax.text = "0%"
                        binding.taxPrice.text = "0"
                    }
                }
                else
                {
                    binding.tax.text = "----"
                    binding.taxPrice.text = "----"
                }
                binding.unitPriceBeforeDiscount.text = item.productPrice
                if (item.product?.hasDiscount == "1" || item.invoiceDiscountType== "1" || item.invoiceDiscountType == "2"){
                    binding.unitPriceAfterDiscount.text = item.totalPrice
                    binding.unitPriceBeforeDiscount.visibility = View.VISIBLE
                    binding.unitPriceAfterDiscount.paintFlags = binding.unitPriceAfterDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                else
                {
                    binding.unitPriceAfterDiscount.visibility = View.GONE
                }
            }

        }
    }
    

}

class ProductDiffCallback : DiffUtil.ItemCallback<ProductInInvoiceModel>() {
    override fun areItemsTheSame(oldItem: ProductInInvoiceModel, newItem: ProductInInvoiceModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductInInvoiceModel, newItem: ProductInInvoiceModel): Boolean {
        return oldItem == newItem
    }

}