package com.codeIn.myCash.ui.home.products.products.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeIn.common.data.Discount
import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemProductBinding
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel

class ProductsAdapter constructor(
    private val context: Context,
    private val communicator: Communicator,
    private val currency : String?
) :
    ListAdapter<ProductModel, ProductsAdapter.ViewHolder>(ProductDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(item = currentItem, context, communicator , currency)
    }


    class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ProductModel,
            context: Context,
            communicator: Communicator,
            currency: String?
        ) {

            binding.apply {
                nameTextView.text = item.name
                val finalPrice = "${item.finalPrice}"
                finalPriceTextView.text = finalPrice

                val originalPrice = "${item.price}"
                originalPriceTextView.text = originalPrice

                if (item.discountType != Discount.None.value.toString()) {
                    if (item.discountType == Discount.Value.value.toString())
                        discountTextView.text = "${item.discount} $currency"
                    else if (item.discountType == Discount.Percentage.value.toString())
                        discountTextView.text = "${item.discount} %"
                    discountTextView.visible()
                    originalPriceTextView.visible()
                }else{
                    discountTextView.gone()
                    originalPriceTextView.gone()
                }

                if (item.count > 0) {
                    val count = "x${item.count}"
                    countTextView.text = count
                    trashImageView.visible()
                    countTextView.visible()
                } else {
                    trashImageView.gone()
                    countTextView.gone()
                }
                Glide.with(context)
                    .load(item.image)
                    .error(R.drawable.icon_app)
                    .into(binding.productImageView)


                if (NumberHelper.persianToEnglishText(item.quantity?:"0").toDouble() == 0.0) {
                    limitStockTextView.visible()
                    limitStockTextView.text = context.getString(R.string.outStock)
                }
                else if (NumberHelper.persianToEnglishText(item.quantity?:"0").toDouble() <= 10){
                    limitStockTextView.visible()
                    limitStockTextView.text = context.getString(R.string.limit_stock)
                }
                else
                {
                    limitStockTextView.gone()
                }

                if (item.count > 0)
                    rootConstraintlayout.setBackgroundResource(R.drawable.bg_main10_r12_s1_secondary)
                else
                    rootConstraintlayout.setBackgroundResource(R.drawable.bg_white_r12_s1_stroke60)

                rootConstraintlayout.setOnClickListener {
                    if ((context as MainActivity).viewModel.isCompleteInfo.value == true){
                      if (NumberHelper.persianToEnglishText(item.quantity?:"0").toDouble() > 0.0){
                          if (!countTextView.text.isNullOrEmpty()){
                              val currentValue = countTextView.text.toString().replace("x" , "")
                              if (NumberHelper.persianToEnglishText(currentValue?:"0.0").toDouble() < NumberHelper.persianToEnglishText(item.quantity?:"0").toDouble()){
                                  item.count++
                                  val count = "x${item.count}"
                                  countTextView.text = count
                                  trashImageView.visible()
                                  countTextView.visible()
                                  communicator.addProductToInvoice(item)
                                  rootConstraintlayout.setBackgroundResource(R.drawable.bg_main10_r12_s1_secondary)
                              }
                          }
                          else {
                              item.count++
                              val count = "x${item.count}"
                              countTextView.text = count
                              trashImageView.visible()
                              countTextView.visible()
                              communicator.addProductToInvoice(item)
                              rootConstraintlayout.setBackgroundResource(R.drawable.bg_main10_r12_s1_secondary)
                          }
                      }
                    }
                    else {
                        CustomToaster.show(
                            context ,
                            context.getString(R.string.please_complete_your_info_before_make_any_invoice),
                            false
                        )
                    }
                }

                trashImageView.setOnClickListener {
                    if ((context as MainActivity).viewModel.isCompleteInfo.value == true){
                        item.count--
                        val count = "x${item.count}"
                        countTextView.text = count
                        if (item.count == 0) {
                            trashImageView.gone()
                            countTextView.gone()
                            rootConstraintlayout.setBackgroundResource(R.drawable.bg_white_r12_s1_stroke60)
                        }
                        communicator.removeProductFromInvoice(item)
                    }
                    else {
                        CustomToaster.show(
                            context ,
                            context.getString(R.string.please_complete_your_info_before_make_any_invoice),
                            false
                        )
                    }

                }

                
                trashImageView.setOnLongClickListener {
                    if ((context as MainActivity).viewModel.isCompleteInfo.value == true){
                        communicator.removeAllProductFromInvoice(item)
                        item.count = 0
                        countTextView.text = item.count.toString()
                        trashImageView.gone()
                        countTextView.gone()
                        rootConstraintlayout.setBackgroundResource(R.drawable.bg_white_r12_s1_stroke60)

                    }
                    else {
                        CustomToaster.show(
                            context ,
                            context.getString(R.string.please_complete_your_info_before_make_any_invoice),
                            false
                        )
                    }

                    true
                }


                rootConstraintlayout.setOnLongClickListener {
                    communicator.showProductOptions(item)
                    true
                }

                optionsProduct.setOnClickListener {
                    communicator.showProductOptions(item)
                }
            }

        }
    }

    interface Communicator {
        fun addProductToInvoice(product: ProductModel)
        fun removeProductFromInvoice(product: ProductModel)
        fun removeAllProductFromInvoice(product: ProductModel)
        fun showProductOptions(product: ProductModel)
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