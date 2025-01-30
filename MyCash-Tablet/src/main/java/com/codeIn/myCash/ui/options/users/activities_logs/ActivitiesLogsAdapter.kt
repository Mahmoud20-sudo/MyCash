package com.codeIn.myCash.ui.options.users.activities_logs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.databinding.ItemActivitiesLogBinding


class ActivitiesLogsAdapter(
    private val communicator: Communicator
) :
    ListAdapter<ActivityLog, ActivitiesLogsAdapter.ViewHolder>(ActivityLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemActivitiesLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(
            item = currentItem, communicator = communicator
        )
    }

    class ViewHolder(val binding: ItemActivitiesLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ActivityLog,
            communicator: Communicator
        ) {

            binding.apply {
                dateTextView.text = item.date
                branchTextView.text = item.branch
                totalSalesTextView.text = item.totalSales
                root.setOnClickListener { communicator.onClick(item) }
            }
        }
    }


    interface Communicator {
        fun onClick(activityLog: ActivityLog)
    }

    private class ActivityLogDiffCallback : DiffUtil.ItemCallback<ActivityLog>() {
        override fun areItemsTheSame(oldItem: ActivityLog, newItem: ActivityLog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ActivityLog, newItem: ActivityLog): Boolean {
            return oldItem == newItem
        }
    }
}

