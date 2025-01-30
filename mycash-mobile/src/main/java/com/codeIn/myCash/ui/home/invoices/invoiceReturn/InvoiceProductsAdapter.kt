package com.codeIn.myCash.ui.home.invoices.invoiceReturn

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
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.utilities.visible
import com.squareup.picasso.Picasso
import java.util.Locale
import kotlin.math.abs

class InvoiceProductsAdapter constructor(
    private val context: Context,
    private val communicator: Communicator,
):
    ListAdapter<ProductInInvoiceModel, InvoiceProductsAdapter.ViewHolder>(ProductDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInvoiceProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem)

        holder.binding.apply {
            val quantityInInvoice = NumberHelper.persianToEnglishText(
                currentItem.quantity?:"0.0"
            )
            increaseImageView.setOnClickListener {
                val currentQuantity = NumberHelper.persianToEnglishText(
                    currentItem.count.toString()
                )
                if (currentQuantity.toDouble() < quantityInInvoice.toDouble()){
                    currentItem.count = currentQuantity.toInt() + 1
                    communicator.updateProductInInvoice(currentItem)
                    holder.binding.countTextView.text = currentItem.count.toString()
                }

            }
            decreaseImageView.setOnClickListener {
                val currentQuantity = NumberHelper.persianToEnglishText(
                    currentItem.count.toString()
                )
                if (currentQuantity.toDouble() > 0) {
                    currentItem.count = currentQuantity.toInt() - 1
                    if (currentQuantity.toDouble() > 1)
                        communicator.updateProductInInvoice(currentItem)
                    else if (currentQuantity.toDouble() == 0.0 )
                        communicator.removeProductFromInvoice(currentItem)
                }
                holder.binding.countTextView.text = currentItem.count.toString()
            }
        }
    }


    inner class ViewHolder(val binding: ItemInvoiceProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductInInvoiceModel) {
            binding.apply {
                if (item.count==0)
                    item.count = item.quantity?.toDouble()?.toInt()?:0
                val unitPrice = NumberHelper.persianToEnglishText(item.unitPrice?:"0.0").toDouble() -
                        NumberHelper.persianToEnglishText(item.unitDiscountPrice?:"0.0").toDouble()
                countTextView.text = (item.count?:0).toString()
                productNameTextView.text = item.product?.name

                productPriceTextView.text = String.format(Locale.ENGLISH,"%.2f", unitPrice)

                item.notificationPrice?.let {
                    when {
                        it.toDouble() == 0.0 -> {
                            tvDebitNoteState.gone()
                            tvCreditNoteState.gone()
                        }

                        it.toDouble() > 0.0 -> {
                            tvDebitNoteState.visible()
                            tvDebitNoteState.text = String.format(
                                Locale.ENGLISH,
                                "%s %s",
                                context.getString(R.string.debit_state),
                                it
                            )
                            tvCreditNoteState.gone()
                        }

                        else -> {
                            tvCreditNoteState.visible()
                            tvCreditNoteState.text = String.format(
                                Locale.ENGLISH,
                                "%s %s",
                                context.getString(R.string.credit_state),
                                abs(it.toDouble()).toString()
                            )
                            tvDebitNoteState.gone()
                        }
                    }
                }


                offerTextView.gone()
                addDiscountTextView.gone()

                Picasso.get()
                    .load(item.product?.image)
                    .error(R.drawable.icon_app)
                    .into(productImageView)
            }

        }
    }

    interface Communicator {
        fun removeProductFromInvoice(product: ProductInInvoiceModel)
        fun updateProductInInvoice(product: ProductInInvoiceModel)
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