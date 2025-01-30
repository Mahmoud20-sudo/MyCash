package com.codeIn.myCash.ui.options.users.user_details

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogUserDetailsMoreBinding


class UsersMoreDialog(
    context: Context,
    private val communicator: Communicator
) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogUserDetailsMoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogUserDetailsMoreBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            totalUserSalesTextView.setOnClickListener {
                communicator.showTotalSalesReport()
                dismiss()
            }
            userActivitiesTextView.setOnClickListener {
                communicator.showUserActivities()
                dismiss()
            }
        }

    }

    interface Communicator {

        fun showTotalSalesReport()
        fun showUserActivities()
    }
}