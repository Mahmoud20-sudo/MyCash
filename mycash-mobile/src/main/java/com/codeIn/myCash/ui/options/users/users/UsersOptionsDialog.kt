package com.codeIn.myCash.ui.options.users.users

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogUserOptionsBinding
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO


class UsersOptionsDialog(
    context: Context,
    private val user: GetUsersDTO.Data.Data,
    private val communicator: Communicator
) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogUserOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogUserOptionsBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.activateSwitch.isChecked = user.isActive == true

        binding.editTextView.setOnClickListener {
            communicator.edit(user)
            dismiss()
        }

        binding.deleteTextView.setOnClickListener {
            communicator.delete(user)
            dismiss()
        }

        binding.activateSwitch.setOnCheckedChangeListener { _, isChecked ->
            user.isActive = isChecked
            communicator.activate(user)
        }
        binding.shareTextView.setOnClickListener {
            communicator.share(user)
        }

    }

    interface Communicator {
        /**
         * Used to edit a user
         */
        fun edit(user: GetUsersDTO.Data.Data)

        /**
         * Used to delete a user
         */
        fun delete(user: GetUsersDTO.Data.Data)

        /**
         * Used to activate a user
         */
        fun activate(user: GetUsersDTO.Data.Data)
        fun share(user : GetUsersDTO.Data.Data)
    }
}