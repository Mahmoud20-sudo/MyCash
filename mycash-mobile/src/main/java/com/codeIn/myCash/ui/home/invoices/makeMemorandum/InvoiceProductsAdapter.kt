package com.codeIn.myCash.ui.home.invoices.makeMemorandum

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeIn.myCash.databinding.ItemInvoiceProductBinding
import com.codeIn.common.util.gone
import com.codeIn.common.util.invisible
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel

class InvoiceProductsAdapter(
    private val communicator: Communicator,
    private val context:  Context,
    private val currency : String?,

    ) : ListAdapter<ProductModel, InvoiceProductsAdapter.ViewHolder>(ProductDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInvoiceProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem, communicator = communicator , context = context , currency = currency)

        holder.binding.apply {
            decreaseImageView.setOnClickListener {
               communicator.removeProductFromMemorandum(currentItem)
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
                specifyAmountTextView.visible()
                increaseImageView.invisible()

                val text = if (item.difPrice.isNullOrEmpty() || item.difPrice.equals("0.0"))
                     context.getString(R.string.specify_amount)
                else
                    item.difPrice


                val content = SpannableString(text)
                content.setSpan( UnderlineSpan(), 0, text?.length?:0, 0)
                specifyAmountTextView.text = content

                specifyAmountTextView.setOnClickListener {
                    communicator.specifyAmount(item)
                }

                Glide.with(context)
                    .load(item.image)
                    .error(R.drawable.icon_app)
                    .into(binding.productImageView)
            }

        }
    }

    interface Communicator {
        fun specifyAmount(product: ProductModel)
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