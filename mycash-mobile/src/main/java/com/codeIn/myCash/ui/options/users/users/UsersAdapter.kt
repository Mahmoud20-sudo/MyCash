package com.codeIn.myCash.ui.options.users.users

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemUserBinding
import com.codeIn.myCash.ui.options.users.users.UsersAdapter.Communicator
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO

/**
 * Adapter for the [RecyclerView] in [UsersFragment].
 * @param communicator [Communicator] interface to handle the click on the user item
 * @see Communicator
 */
class UsersAdapter(
    private val communicator: Communicator
) :
    ListAdapter<GetUsersDTO.Data.Data, UsersAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(
            item = currentItem, communicator = communicator
        )

    }


    class ViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: GetUsersDTO.Data.Data,
            communicator: Communicator
        ) {

            binding.apply {

                nameTextView.text = item.name
                emailTextView.text = item.email
                phoneTextView.text =item.phone
                branchesTextView.text =item.country.toString()
                val drawableStart = ResourcesCompat.getDrawable(
                    nameTextView.context.resources,
                    R.drawable.ic_tag_user20,
                    null
                )
                val (name, color, drawable) = when (item.isMain) {
                    true -> Triple(
                        item.branch?.name?:nameTextView.context.resources.getString(R.string.branch_name),
                        ContextCompat.getColor(nameTextView.context, R.color.secondaryColor),
                        ResourcesCompat.getDrawable(
                            nameTextView.context.resources,
                            R.drawable.ic_magic_star,
                            null
                        )
                    )

                    else -> Triple(
                        item.branch?.name?:nameTextView.context.resources.getString(R.string.branch),
                        ContextCompat.getColor(nameTextView.context, R.color.mainBlack),
                        null
                    )
                }
                branchesTextView.text = name
                nameTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawableStart,
                    null,
                    drawable,
                    null
                )
                nameTextView.changeDrawableAndTextColors(color)

                moreImageView.setOnClickListener {
                    communicator.onLongClick(item)
                }


                root.setOnLongClickListener {
                    communicator.onLongClick(item)
                    true
                }
                root.setOnClickListener { communicator.onClick(item) }
            }

        }
    }


    /**
     * Interface for handling user interactions.
     *
     * This interface defines two methods that should be implemented to handle
     * click and long click actions on a user item in the RecyclerView.
     */
    interface Communicator {
        /**
         * Called when a user item in the RecyclerView is long clicked.
         *
         * @param user The user item that was long clicked.
         */
        fun onLongClick(user: GetUsersDTO.Data.Data)

        /**
         * Called when a user item in the RecyclerView is clicked.
         *
         * @param user The user item that was clicked.
         */
        fun onClick(user: GetUsersDTO.Data.Data)
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<GetUsersDTO.Data.Data>() {
        override fun areItemsTheSame(oldItem: GetUsersDTO.Data.Data, newItem: GetUsersDTO.Data.Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GetUsersDTO.Data.Data, newItem: GetUsersDTO.Data.Data): Boolean {
            return oldItem == newItem
        }
    }

    fun deleteUser(user: GetUsersDTO.Data.Data){
        val newUsersList = currentList.toMutableList()
        newUsersList.remove(user)
        submitList(newUsersList)
    }
}