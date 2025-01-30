package com.codeIn.myCash.utilities.pdf_manager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.databinding.ItemPdfProductDetailsBinding
import com.codeIn.myCash.ui.home.products.products.Product

class PdfProductDetailsAdapter :
    ListAdapter<Product, PdfProductDetailsAdapter.ViewHolder>(
        PdfProductDetailsDiffCallback()
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPdfProductDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(
            item = currentItem
        )

    }


    class ViewHolder(val binding: ItemPdfProductDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Product,
        ) {

            binding.apply {
                descriptionTextView.text = item.desc
                unitPriceTextView.text = item.price
                quantityTextView.text = item.count.toString()
                discountTextView.text = item.discount
                totalBeforeVatTextView.text = item.priceAfterDiscount
                vatTextView.text = item.tax
                vatAmountTextView.text = item.tax
                totalIncludingVatTextView.text = item.finalPriceAfterDiscount
            }

        }
    }
    class PdfProductDetailsDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }

    }
}

