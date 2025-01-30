package com.codeIn.myCash.ui.home.products.first_step_create_invoice

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeIn.common.data.Discount
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.myCash.databinding.ItemInvoiceProductBinding
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.squareup.picasso.Picasso

class InvoiceProductsAdapter constructor(
    private val context: Context,
    private val communicator: Communicator,
    private  val currency : String? ,
    private val invoiceType : String?
) :
    ListAdapter<ProductModel, InvoiceProductsAdapter.ViewHolder>(ProductDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInvoiceProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem , context, currency , invoiceType)

        holder.binding.apply {
            increaseImageView.setOnClickListener {
                currentItem.count++
                communicator.addProductToInvoice(currentItem)
                holder.binding.countTextView.text = currentItem.count.toString()
            }
            decreaseImageView.setOnClickListener {
                if (currentItem.count >= 0) {
                    currentItem.count--
                    if (currentItem.count >= 1)
                        communicator.updateProductInInvoice(currentItem)
                    else
                        communicator.removeProductFromInvoice(currentItem)
                }
                holder.binding.countTextView.text = currentItem.count.toString()
            }
            decreaseImageView.setOnLongClickListener {
                communicator.removeAllProductFromInvoice(currentItem)
                currentItem.count = 0
                holder.binding.countTextView.text = currentItem.count.toString()
                true
            }
            addDiscountTextView.setOnClickListener {
                communicator.addDiscount(currentItem)
            }
        }


    }


    class ViewHolder(val binding: ItemInvoiceProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductModel, context: Context ,  currency: String? , invoiceType : String?) {
            binding.apply {
                countTextView.text = item.count.toString()
                productNameTextView.text = item.name
                productPriceTextView.text = item.finalPrice

                Picasso.get()
                    .load(item.image)
                    .error(R.drawable.icon_app)
                    .into(productImageView)

                if (item.hasDiscount != "0") {
                    if (item.discountType == Discount.Percentage.value.toString())
                        offerTextView.text ="${item.discount}%"
                    else if (item.discountType == Discount.Value.value.toString())
                        offerTextView.text = "${item.discount}$currency"
                    offerTextView.visible()
                }else{
                    offerTextView.gone()
                }
            }

            if (invoiceType == MainTypeInvoice.PURCHASE.value.toString())
                binding.addDiscountTextView.gone()
            else
                binding.addDiscountTextView.visible()

        }
    }

    interface Communicator {
        fun addProductToInvoice(product: ProductModel)
        fun removeProductFromInvoice(product: ProductModel)
        fun updateProductInInvoice(product: ProductModel)
        fun removeAllProductFromInvoice(product: ProductModel)
        fun addDiscount(product: ProductModel)
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