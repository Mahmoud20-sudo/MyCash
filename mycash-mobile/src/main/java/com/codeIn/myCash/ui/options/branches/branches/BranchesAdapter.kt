package com.codeIn.myCash.ui.options.branches.branches

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemBranchBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO

/**
 * [BranchesAdapter] is the adapter class for the [BranchesFragment] recycler view
 * @param communicator [Communicator] the interface that will handle the click on the Notification item
 */
class BranchesAdapter constructor(
    private val communicator: Communicator,
    private val context: Context
) :
    ListAdapter<BranchesDTO.Data.Data, BranchesAdapter.ViewHolder>(BranchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBranchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(item = currentItem, communicator = communicator , context= context)
    }

    class ViewHolder(val binding: ItemBranchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: BranchesDTO.Data.Data,
            communicator: Communicator,
            context: Context
        ) {
            binding.apply {
                titleTextView.text = item.name
                subTitleTextView.text = item.address
                codeTitleTextView.text = item.phone?:"----"

                when (item.isMain) {
                    1 -> { // Assuming 1 represents true
                        iconImageView.setImageResource(R.drawable.ic_shop)
                        iconImageView.colorFilter = null
                    }
                    else -> {
                        iconImageView.setImageResource(R.drawable.ic_location)
                        iconImageView.colorFilter = null
                    }
                }

                moreImageView.setOnClickListener {
                    communicator.onLongClick(item)
                }
                codeTitleTextView.setOnClickListener {
                    communicator.branchDetailsClick(item)
                }

                rootLayout.setOnClickListener {
                    communicator.branchDetailsClick(item)
                }

                if(item.status == 1){
                    receiptTextView.text = context.getString(R.string.activate_branch)
                }
                else {
                    receiptTextView.text = context.getString(R.string.not_activated)
                }

            }
        }
    }

    /**
     * interface to handle the click on the branch item
     */
    interface Communicator {
        /**
         * function to handle the click on the branch item
         * @param branch [BranchesDTO.Data.Data] the branch that the user clicked on
         * @see BranchesDTO.Data.Data
         */
        fun onLongClick(branch: BranchesDTO.Data.Data)
        fun branchDetailsClick(branch: BranchesDTO.Data.Data)

    }

    private class BranchDiffCallback : DiffUtil.ItemCallback<BranchesDTO.Data.Data>() {
        override fun areItemsTheSame(oldItem: BranchesDTO.Data.Data, newItem: BranchesDTO.Data.Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BranchesDTO.Data.Data, newItem: BranchesDTO.Data.Data): Boolean {
            return oldItem == newItem
        }
    }
}

