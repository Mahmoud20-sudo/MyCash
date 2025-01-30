package com.codeIn.myCash.ui.home.reports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.databinding.ItemProductsQuantitiesReportBinding
import com.plcoding.reports.data.productsQuantitiesReport.model.beans.ProductsQuantity
import javax.inject.Inject

class ProductsQuantitiesReportAdapter @Inject constructor() :
    RecyclerView.Adapter<ProductsQuantitiesReportAdapter.SalesInvoicesViewHolder>() {

    private var dataSet: List<ProductsQuantity> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null
    private lateinit var context: Context

    fun submitProductsQuantities(data: List<ProductsQuantity>) {
        dataSet = data
    }

    fun onItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class SalesInvoicesViewHolder(val binding: ItemProductsQuantitiesReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductsQuantity) {
            binding.apply {
                productName.text = item.productName
                quantity.text = item.quantity.toString()
                branchName.text = item.branchName
                categoryName.text = item.category

                edit.setOnClickListener {
                    onItemClickListener?.onEditProductClick(item.productId)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesInvoicesViewHolder {
        context = parent.context
        val binding =
            ItemProductsQuantitiesReportBinding.inflate(LayoutInflater.from(context), parent, false)
        return SalesInvoicesViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: SalesInvoicesViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    interface OnItemClickListener {
        fun onEditProductClick(productId: Int)
    }
}