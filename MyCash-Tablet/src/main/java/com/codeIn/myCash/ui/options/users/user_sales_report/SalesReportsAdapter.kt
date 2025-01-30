package com.codeIn.myCash.ui.options.users.user_sales_report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.databinding.ItemSalesReportBinding
import com.codeIn.myCash.ui.options.users.users.UsersAdapter.Communicator


class SalesReportsAdapter(
    private val communicator: Communicator
) :
    ListAdapter<SalesReport, SalesReportsAdapter.ViewHolder>(SalesReportDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSalesReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(
            item = currentItem, communicator = communicator
        )
    }

    class ViewHolder(val binding: ItemSalesReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: SalesReport,
            communicator: Communicator
        ) {

            binding.apply {
                titleTextView.text = item.title
                valueTextView.text = item.value
                root.setOnClickListener { communicator.onClick(item) }
            }
        }
    }


    interface Communicator {
        fun onClick(salesReport: SalesReport)
    }

    private class SalesReportDiffCallback : DiffUtil.ItemCallback<SalesReport>() {
        override fun areItemsTheSame(oldItem: SalesReport, newItem: SalesReport): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SalesReport, newItem: SalesReport): Boolean {
            return oldItem == newItem
        }
    }
}

