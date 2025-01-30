package com.codeIn.myCash.ui.home.products.products.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemCategoryMainBinding
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData

class CategoriesMainAdapter(
    context: Context,
    private val communicator: Communicator
) :
    ListAdapter<CategoryData, CategoriesMainAdapter.ViewHolder>(CategoryDiffCallback()) {


    private var selectedItem: CategoryData? = null

    private val backgroundRes = R.drawable.bg_white_r12_s2_stroke_ripple

    private val backgroundResSelected = R.drawable.bg_white_r12_s2_secondary_ripple

    private val textColor = ContextCompat.getColor(context, R.color.mainBlack)
    private val textColorSelected = ContextCompat.getColor(context, R.color.secondaryColor)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

        if (currentItem.selected == true) {
            selectedItem = currentItem
            holder.binding.root.setBackgroundResource(backgroundResSelected)
            holder.binding.nameTextView.changeDrawableAndTextColors(textColorSelected)
        } else {
            holder.binding.root.setBackgroundResource(backgroundRes)
            holder.binding.nameTextView.changeDrawableAndTextColors(textColor)
        }

    }


    class ViewHolder(val binding: ItemCategoryMainBinding) :
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

