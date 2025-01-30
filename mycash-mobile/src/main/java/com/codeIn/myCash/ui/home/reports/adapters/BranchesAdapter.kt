package com.codeIn.myCash.ui.home.reports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.BranchListItemBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO

class BranchesAdapter : RecyclerView.Adapter<BranchesAdapter.SalesInvoicesViewHolder>() {

    private var dataSet: List<BranchesDTO.Data.Data> = emptyList()
    private var onItemClickListener: OnBranchClickListener? = null
    private lateinit var context: Context
    private var selectedBranch = 0

    fun submitBranches(data: List<BranchesDTO.Data.Data>, selectedBranchPosition: Int = 0) {
        dataSet = data
        this.selectedBranch = selectedBranchPosition
    }

    fun onBranchClickListener(listener: OnBranchClickListener) {
        onItemClickListener = listener
    }

    inner class SalesInvoicesViewHolder(val binding: BranchListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BranchesDTO.Data.Data, position: Int) {
            binding.apply {
                branchName.text = item.name

                if (selectedBranch == position) {
                    root.setBackgroundColor(context.resources.getColor(R.color.off_white, null))
                    onItemClickListener?.onBranchClick(item, bindingAdapterPosition)

                } else root.setBackgroundColor(context.resources.getColor(R.color.white, null))


                root.setOnClickListener {
                    if (selectedBranch == position) return@setOnClickListener
                    val previousSection = selectedBranch
                    selectedBranch = bindingAdapterPosition

                    notifyItemChanged(previousSection)
                    notifyItemChanged(selectedBranch)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesInvoicesViewHolder {
        context = parent.context
        val binding =
            BranchListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return SalesInvoicesViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: SalesInvoicesViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item, position)
    }

    interface OnBranchClickListener {
        fun onBranchClick(branch: BranchesDTO.Data.Data, position: Int)
    }
}