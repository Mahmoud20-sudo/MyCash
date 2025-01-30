package com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemProductInventoryReportBinding
import com.plcoding.reports.data.inventory.model.InventoryModel

class ProductInventoryReportAdapter : RecyclerView.Adapter<ProductInventoryReportAdapter.InventoryViewHolder>() {

    class InventoryViewHolder(var binding: ItemProductInventoryReportBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    private val diffUtil = object : DiffUtil.ItemCallback<InventoryModel>() {
        override fun areItemsTheSame(oldItem: InventoryModel, newItem: InventoryModel):
                Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InventoryModel, newItem: InventoryModel):
                Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryViewHolder {
        val binding = ItemProductInventoryReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return InventoryViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun saveData( dataResponse: List<InventoryModel>){
        val currentList = asyncListDiffer.currentList.toMutableList()
        currentList.addAll(dataResponse)
        asyncListDiffer.submitList(currentList)
    }

    fun clear(){
        asyncListDiffer.submitList(null)
    }

    override fun onBindViewHolder(
        holder: InventoryViewHolder, position: Int
    ) {
        val item = asyncListDiffer.currentList[position]
        val context = holder.binding.root.context
        holder.binding.apply {
            dateTvValueTv.text = item.createdAt
            totalValueTv.text = context.getString(R.string.string_sar, item.product?.productPrice)
        }
    }
}