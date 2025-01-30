package com.codeIn.myCash.ui.options.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.help.data.model.response.HowWorkDTO
import com.codeIn.myCash.databinding.GuideHelpItemBinding

class HowWorkAdapter constructor(
    private val communicator: Communicator,
) :
    ListAdapter<HowWorkDTO.Data.Data, HowWorkAdapter.ViewHolder>(HelpDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GuideHelpItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(item = currentItem, communicator = communicator)
    }

    class ViewHolder(val binding: GuideHelpItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: HowWorkDTO.Data.Data,
            communicator: Communicator,
        ) {
            binding.apply {
                titleTv.text = item.titleAr
                card.setOnClickListener {
                    communicator.onCardClick(item)
                }
            }
        }
    }

    /**
     * interface to handle the click on the branch item
     */
    interface Communicator {

        fun onCardClick(item: HowWorkDTO.Data.Data)
    }

    private class HelpDiffCallback : DiffUtil.ItemCallback<HowWorkDTO.Data.Data>() {
        override fun areItemsTheSame(oldItem: HowWorkDTO.Data.Data, newItem: HowWorkDTO.Data.Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HowWorkDTO.Data.Data, newItem: HowWorkDTO.Data.Data): Boolean {
            return oldItem == newItem
        }
    }
}
