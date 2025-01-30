package com.codeIn.myCash.ui.home.products.products.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemCategoryBinding
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData

class CategoriesAdapter(
    context: Context,
    private val communicator: Communicator
) : ListAdapter<CategoryData, CategoriesAdapter.ViewHolder>(CategoryDiffCallback()) {


    private var selectedItem: CategoryData? = null

    private val backgroundRes = R.drawable.bg_white_r100_s1_stroke_ripple

    private val backgroundResSelected = R.drawable.bg_white_r100_s1_secondary_ripple

    private val textColor = ContextCompat.getColor(context, R.color.mainBlack)
    private val textColorSelected = ContextCompat.getColor(context, R.color.secondaryColor)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(
            item = currentItem
        )

        holder.binding.root.setOnClickListener {
            selectedItem?.selected = false
            currentItem.selected = true
            communicator.setCategory(currentItem)

            notifyItemChanged(position)
            notifyItemChanged(currentList.indexOf(selectedItem))

            selectedItem = currentItem
        }

        if (currentItem.selected) {
            selectedItem = currentItem
            holder.binding.root.setBackgroundResource(backgroundResSelected)
            holder.binding.nameTextView.changeDrawableAndTextColors(textColorSelected)
        } else {
            holder.binding.root.setBackgroundResource(backgroundRes)
            holder.binding.nameTextView.changeDrawableAndTextColors(textColor)
        }

    }


    class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CategoryData,
        ) {

            binding.apply {
                nameTextView.text = item.name
            }

        }
    }

    interface Communicator {
        fun setCategory(category: CategoryData)
    }

}

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryData>() {
    override fun areItemsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
        return oldItem == newItem
    }

}