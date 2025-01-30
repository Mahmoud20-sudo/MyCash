package com.codeIn.myCash.ui.home.invoices.addMoreProductsToMemorandum

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeIn.myCash.databinding.ItemInvoiceProductBinding
import com.codeIn.common.util.gone
import com.codeIn.myCash.R
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel

class InvoiceProductsAdapter (
    private val communicator: Communicator,
    private val context: Context,
    private val currency : String?,
):ListAdapter<ProductModel, InvoiceProductsAdapter.ViewHolder>(ProductDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInvoiceProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(item = currentItem , communicator = communicator , context = context , currency = currency)
        holder.binding.apply {
            increaseImageView.setOnClickListener {
                currentItem.count++
                communicator.addProductToMemorandum(currentItem)
                holder.binding.countTextView.text = currentItem.count.toString()
            }
            decreaseImageView.setOnClickListener {
                if (currentItem.count > 0) {
                    currentItem.count--
                    if (currentItem.count > 1)
                        communicator.updateProductInMemorandum(currentItem)
                    else if (currentItem.count == 0 )
                        communicator.removeProductFromMemorandum(currentItem)
                }
                holder.binding.countTextView.text = currentItem.count.toString()
            }
        }
    }


    class ViewHolder(val binding: ItemInvoiceProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductModel, communicator: Communicator, context: Context, currency: String?) {
            binding.apply {
                countTextView.text = item.count.toString()
                productNameTextView.text = item.name
                productPriceTextView.text = "${item.productPriceAfterDiscount} $currency"
                offerTextView.gone()
                addDiscountTextView.gone()
                specifyAmountTextView.gone()

                Glide.with(context)
                    .load(item.image)
                    .error(R.drawable.icon_app)
                    .into(binding.productImageView)
            }
        }
    }

    interface Communicator {
        fun addProductToMemorandum(product: ProductModel)
        fun updateProductInMemorandum(product: ProductModel)
        fun removeProductFromMemorandum(product: ProductModel)
    }
}
class ProductDiffCallback : DiffUtil.ItemCallback<ProductModel>() {
    override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
        return oldItem == newItem
    }

}