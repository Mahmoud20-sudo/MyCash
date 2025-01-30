package com.codeIn.myCash.ui.home.expenses.expenses

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.util.toTwoDigits
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemExpensesBinding
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseModel

class ExpensesAdapter constructor(private val context: Context, private val communicator: Communicator, private val currency : String?) :
    ListAdapter<ExpenseModel, ExpensesAdapter.ViewHolder>(ExpenseDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExpensesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(item = currentItem,context = context,communicator = communicator , currency=currency)

    }


    class ViewHolder(val binding: ItemExpensesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ExpenseModel,
            context: Context,
            communicator: Communicator,
            currency: String?
        ) {

            binding.apply {
                titleTextView.text = item.statement?:"----"
                dateTextView.text = item.date
                expensesPriceTextView.text =  "${NumberHelper.persianToEnglishText(item.totalAmount ?:"0.0").toDouble().toTwoDigits()} $currency"
            }

            binding.root.setOnClickListener {
                communicator.onItemClicked(item)
            }

        }
    }

    interface Communicator {
        fun onItemClicked(item: ExpenseModel)
    }

    class ExpenseDiffCallback : DiffUtil.ItemCallback<ExpenseModel>() {
        override fun areItemsTheSame(oldItem: ExpenseModel, newItem: ExpenseModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExpenseModel, newItem: ExpenseModel): Boolean {
            return oldItem == newItem
        }

    }

}

