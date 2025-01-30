package com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemClientBinding
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel

class ClientsAdapter constructor(private val communicator: Communicator, var type : ClientsSection) :
    ListAdapter<ClientModel, ClientsAdapter.ViewHolder>(ClientDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(
            item = currentItem, communicator = communicator , type = type
        )
    }


    class ViewHolder(val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ClientModel,
            communicator: Communicator,
            type: ClientsSection
        ) {

            binding.apply {
                nameTextView.text = item.name
                if (type == ClientsSection.CLIENTS){
                    secondTextView.text = item.email
                    if (item.totalUnPaid?.isNotEmpty() == true && item.totalUnPaid.equals("0.0")) {
                        receiptLayout.visible()
                        receiptTextView.text = item.totalUnPaid
                    } else {
                        receiptLayout.gone()
                    }
                }
                else if (type == ClientsSection.VENDORS){
                    val tax = item.taxRecord?: "----"
                    secondTextView.text = "${binding.root.context.resources.getString(R.string.tax_registration_number)} : $tax"
                    receiptLayout.gone()
                }
                phoneTextView.text = item.phone
                root.setOnClickListener {
                    communicator.showDetails(item)
                }
            }

        }
    }

    interface Communicator {
        fun showDetails(client: ClientModel)
    }

}

class ClientDiffCallback : DiffUtil.ItemCallback<ClientModel>() {
    override fun areItemsTheSame(oldItem: ClientModel, newItem: ClientModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ClientModel, newItem: ClientModel): Boolean {
        return oldItem == newItem
    }

}