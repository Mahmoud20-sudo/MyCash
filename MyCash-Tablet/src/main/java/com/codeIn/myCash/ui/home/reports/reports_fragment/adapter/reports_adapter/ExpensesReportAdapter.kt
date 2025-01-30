package com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemExpensesReportBinding
import com.plcoding.reports.data.expense.model.ExpenseModel

class ExpensesReportAdapter : RecyclerView.Adapter<ExpensesReportAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(var binding: ItemExpensesReportBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    private val diffUtil = object : DiffUtil.ItemCallback<ExpenseModel>() {
        override fun areItemsTheSame(oldItem: ExpenseModel, newItem: ExpenseModel):
                Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExpenseModel, newItem: ExpenseModel):
                Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ExpenseViewHolder {
        val binding = ItemExpensesReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExpenseViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun saveData(dataResponse: List<ExpenseModel>) {
        val currentList = asyncListDiffer.currentList.toMutableList()
        currentList.addAll(dataResponse)
        asyncListDiffer.submitList(dataResponse)
    }

    fun clear(){
        asyncListDiffer.submitList(null)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        val context = holder.binding.root.context
        holder.binding.apply {
            expensesCountTv.text = "${item.id}"
            expensesNumberValueTv.text = "${item.id}"
            expensesValueTv.text = context.getString(R.string.expense_value, item.statement)
            valueOfExpensesValueTv.text = context.getString(R.string.string_sar, item.totalAmount)
            timeValueTv.text = item.createdAt
        }
    }
}