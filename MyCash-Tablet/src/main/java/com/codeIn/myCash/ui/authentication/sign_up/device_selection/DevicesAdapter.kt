package com.codeIn.myCash.ui.authentication.sign_up.device_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemDeviceBinding
import com.codeIn.myCash.utilities.addSar
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel

class DevicesAdapter(private val communicator: Communicator) :
    ListAdapter<DeviceModel, DevicesAdapter.ViewHolder>(DeviceDiffCallback()) {

    private var previousItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(
            item = currentItem
        )
        holder.binding.root.setOnClickListener {
            if (currentItem.isSelected) return@setOnClickListener
            setSelection(currentItem, position)
            communicator.onDeviceSelected(currentItem)
        }
    }

    private fun setSelection(currentItem: DeviceModel, position: Int) {
        currentList.getOrNull(previousItemPosition)?.isSelected = false
        currentItem.isSelected = true
        if (previousItemPosition != -1)
            notifyItemChanged(previousItemPosition)
        notifyItemChanged(position)
        previousItemPosition = position
    }


    class ViewHolder(val binding: ItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DeviceModel) {
            val context = binding.root.context
            binding.apply {
                Glide.with(context)
                    .load(item.device?.image)
                    .into(deviceImageView)
                deviceDescTextView.text = context.getString(R.string.device_desc, item.device?.name, item.device?.model)
                deviceCompanyTextView.text = item.device?.brand?.name
                packagePriceTextView.text = item.price?.addSar(context)

                if (item.isSelected) {
                    deviceLayout.setBackgroundResource(R.drawable.bg_white_r12_s2_secondary_ripple)
                } else {
                    deviceLayout.setBackgroundResource(R.drawable.bg_white_r12_s2_stroke_ripple)
                }
            }
        }
    }

    interface Communicator {
        fun onDeviceSelected(device: DeviceModel)
    }
}

class DeviceDiffCallback : DiffUtil.ItemCallback<DeviceModel>() {
    override fun areItemsTheSame(oldItem: DeviceModel, newItem: DeviceModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DeviceModel, newItem: DeviceModel): Boolean {
        return oldItem == newItem
    }

}