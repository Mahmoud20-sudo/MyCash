package com.codeIn.myCash.ui.options.drafts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemDraftBinding

/**
 * [DraftsAdapter] is the adapter class for the drafts list recycler view
 * @param communicator [Communicator] the interface that will handle the click on the Draft item
 */
class DraftsAdapter constructor(
    private val communicator: Communicator
) :
    ListAdapter<Draft, DraftsAdapter.ViewHolder>(DraftsDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(
            item = currentItem, communicator = communicator
        )

    }


    class ViewHolder(val binding: ItemDraftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Draft,
            communicator: Communicator
        ) {

            val context: Context = binding.root.context
            binding.apply {

                subTitleTextView.text = item.subTitle
                extraInfoTextView.text = item.date
                when(item.type){
                    1 ->{
                        titleTextView.text = context.getString(R.string.adding_product_process)
                        iconImageView.setImageResource(R.drawable.ic_note)
                    }
                    2 ->{
                        titleTextView.text = context.getString(R.string.Invoice_creation_process)
                        iconImageView.setImageResource(R.drawable.ic_receipt)
                    }
                    3 ->{
                        titleTextView.text = context.getString(R.string.adding_client_process)
                        iconImageView.setImageResource(R.drawable.ic_people)
                    }
                    4->{
                        titleTextView.text = context.getString(R.string.adding_user_process)
                        iconImageView.setImageResource(R.drawable.ic_profile)
                    }
                }

                deleteImageView.setOnClickListener {
                    communicator.deleteDraft(item)
                }

                root.setOnClickListener {
                    communicator.onClick(item)
                }
            }

        }
    }

    /**
     * interface to handle the click on the draft item
     */
    interface Communicator {
        /**
         * function to handle the click on the draft item in the recycler view.
         * @param draft [Draft] the draft that was clicked
         * @return [Unit]
         * @see Draft
         */
        fun onClick(draft: Draft)

        /**
         * function to handle the click on the delete button in the draft item in the recycler view.
         * @param draft [Draft] the draft that was clicked
         * @return [Unit]
         * @see Draft
         */
        fun deleteDraft(draft: Draft)
    }
    private class DraftsDiffCallback : DiffUtil.ItemCallback<Draft>() {
        override fun areItemsTheSame(oldItem: Draft, newItem: Draft): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Draft, newItem: Draft): Boolean {
            return oldItem == newItem
        }
    }
}

