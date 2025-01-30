package com.codeIn.myCash.ui.authentication.sign_up.subscriptions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.databinding.ItemSubscriptionFeatureBinding
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Feature
/**
 * A simple ListAdapter to show the subscription features in a recycler view.
 * @see SubscriptionItemFragment
 */
class FeaturesAdapter :
    ListAdapter<Feature, FeaturesAdapter.ViewHolder>(FeaturesDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSubscriptionFeatureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(
            item = currentItem
        )


    }


    class ViewHolder(private val binding: ItemSubscriptionFeatureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Feature,
        ) {

            binding.apply {
                nameTextView.text = item.name
            }
        }
    }

}

class FeaturesDiffCallback : DiffUtil.ItemCallback<Feature>() {
    override fun areItemsTheSame(oldItem: Feature, newItem: Feature): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Feature, newItem: Feature): Boolean {
        return oldItem == newItem
    }

}