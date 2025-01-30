package com.codeIn.myCash.ui.options.settings.device_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.databinding.ItemDeviceFeatureBinding
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceFeature

class FeaturesAdapter constructor() :
    ListAdapter<DeviceFeature, FeaturesAdapter.ViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDeviceFeatureBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(item = currentItem)

    }


    class ViewHolder(val binding: ItemDeviceFeatureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DeviceFeature) {

            binding.apply {
                feature.text = item.nameAr
            }
        }
    }


    class ExpenseDiffCallback : DiffUtil.ItemCallback<DeviceFeature>() {
        override fun areItemsTheSame(oldItem: DeviceFeature, newItem: DeviceFeature): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DeviceFeature, newItem: DeviceFeature): Boolean {
            return oldItem == newItem
        }

    }

}

