package com.codeIn.myCash.ui.home.reports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemChunkTypeBinding
import com.plcoding.reports.data.enums.PerChunk
import io.nearpay.sdk.utils.textColor

class PerChunkFiltersAdapter(
    private val dataSet: List<PerChunk>
) : RecyclerView.Adapter<PerChunkFiltersAdapter.ViewHolder>() {

    private var perChunkClickListener: PerChunkClickListener? = null
    private var selectedChunkType = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemChunkTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setPerChunkClicked(perChunkClickListener: PerChunkClickListener) {
        this.perChunkClickListener = perChunkClickListener
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], selectedChunkType == position)
    }

    inner class ViewHolder(private val binding: ItemChunkTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context

        fun bind(item: PerChunk, isSelected: Boolean) {
            binding.apply {
                perChunkType.text = context.getString(item.chunkName)
                perChunkType.setOnClickListener(::onFilterClicked)

                if (isSelected) {
                    perChunkType.setBackgroundResource(R.drawable.bg_primary500_r30)
                    perChunkType.textColor(context, R.color.white)
                    perChunkClickListener?.onPerChunkClicked(dataSet[selectedChunkType])
                } else {
                    perChunkType.setBackgroundResource(R.drawable.bg_stroke_neutral500_r30)
                    perChunkType.textColor(context, R.color.neutral500)
                }
            }
        }

        private fun onFilterClicked(view: View?) {
            if (selectedChunkType == bindingAdapterPosition) return

            val previousSelected = selectedChunkType
            selectedChunkType = bindingAdapterPosition

            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedChunkType)
        }
    }

    interface PerChunkClickListener {
        fun onPerChunkClicked(perChunk: PerChunk)
    }
}