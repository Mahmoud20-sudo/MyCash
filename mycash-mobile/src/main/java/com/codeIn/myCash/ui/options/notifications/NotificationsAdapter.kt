package com.codeIn.myCash.ui.options.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemDraftBinding

/**
 * [NotificationsAdapter] is the adapter class for the notifications list in the [NotificationsFragment]
 * @param communicator [Communicator] the interface that will handle the click on the Notification item
 */
class NotificationsAdapter constructor(
    private val communicator: Communicator
) :
    ListAdapter<Notification, NotificationsAdapter.ViewHolder>(NotificationDiffCallback()) {


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
            item: Notification,
            communicator: Communicator
        ) {

            binding.apply {

                titleTextView.text = item.title
                subTitleTextView.text = item.subTitle
                extraInfoTextView.text = item.date

                iconImageView.setImageResource(R.drawable.ic_status)

                deleteImageView.setOnClickListener {
                    communicator.deleteNotification(item)
                }

                root.setOnClickListener {
                    communicator.onClick(item)
                }
            }

        }
    }

    /**
     * interface to handle the click on the notification item
     */
    interface Communicator {
        /**
         * function to handle the click on the notification item
         * @param notification [Notification] the notification that was clicked
         * @return [Unit]
         */
        fun onClick(notification: Notification)

        /**
         * function to handle the click on the delete icon of the notification item
         * @param notification [Notification] the notification that was clicked
         * @return [Unit]
         */
        fun deleteNotification(notification: Notification)
    }
    private class NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }
}

