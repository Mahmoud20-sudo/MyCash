package com.codeIn.myCash.ui.home.products.products.quickInvoice

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.databinding.ItemInvoiceProductBinding
import com.codeIn.common.util.gone
import com.codeIn.myCash.R
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.squareup.picasso.Picasso

class QuickInvoiceProductsAdapter constructor(
    private val context: Context,
    private val communicator: Communicator
) :
    ListAdapter<ProductInQuickInvoice, QuickInvoiceProductsAdapter.ViewHolder>(ProductDiffCallback()) {

     private lateinit var currency: String

     fun setCurrency(currency: String){
         this.currency = currency
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInvoiceProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem , context, currency)

        holder.binding.apply {
            increaseImageView.setOnClickListener {
                var count = NumberHelper.persianToEnglishText(currentItem.quantity?:"0").toInt()
                count += 1
                currentItem.quantity = count.toString()
                communicator.updateProductInInvoice(currentItem)
                holder.binding.countTextView.text = count.toString()
            }

            decreaseImageView.setOnClickListener {
                val currentCount = NumberHelper.persianToEnglishText(currentItem.quantity?:"0")
                if (currentCount.toInt() > 0) {
                    var count = NumberHelper.persianToEnglishText(currentItem.quantity?:"0").toInt()
                    count -= 1

                    if (count >= 1)
                    {
                        currentItem.quantity = count.toString()
                        communicator.updateProductInInvoice(currentItem)
                        holder.binding.countTextView.text = count.toString()
                    }
                    else if (count == 0 )
                        communicator.removeProductFromInvoice(currentItem)

                    holder.binding.countTextView.text = count.toString()
                }
                else
                    holder.binding.countTextView.text = currentItem.quantity.toString()

            }

            decreaseImageView.setOnLongClickListener {
                communicator.removeProductFromInvoice(currentItem)
                true
            }
        }


    }


    class ViewHolder(val binding: ItemInvoiceProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductInQuickInvoice, context: Context ,  currency: String?) {
            binding.apply {
                countTextView.text = item.quantity.toString()
                productNameTextView.text = item.name
                productPriceTextView.text = "${item.price} $currency"
                Picasso.get()
                    .load(R.drawable.icon_app)
                    .error(R.drawable.icon_app)
                    .into(productImageView)
                offerTextView.gone()
                addDiscountTextView.gone()
            }

        }
    }

    interface Communicator {
        fun removeProductFromInvoice(product: ProductInQuickInvoice)
        fun updateProductInInvoice(product: ProductInQuickInvoice)
    }

}

class ProductDiffCallback : DiffUtil.ItemCallback<ProductInQuickInvoice>() {
    override fun areItemsTheSame(oldItem: ProductInQuickInvoice, newItem: ProductInQuickInvoice): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ProductInQuickInvoice, newItem: ProductInQuickInvoice): Boolean {
        return oldItem == newItem
    }

}