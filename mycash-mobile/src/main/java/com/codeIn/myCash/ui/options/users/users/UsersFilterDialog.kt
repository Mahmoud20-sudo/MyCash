package com.codeIn.myCash.ui.options.users.users

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentManager
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogUsersFilterBinding
import com.codeIn.myCash.utilities.pickers.DatePicker
import com.codeIn.myCash.features.user.data.authentication.remote.response.Branch


class UsersFilterDialog(
    context: Context,
    private val branches: List<Branch>,
    private val fragmentManager: FragmentManager,
    private val communicator: Communicator
) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogUsersFilterBinding
    private val datePicker = DatePicker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogUsersFilterBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item, branches
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.branchesSpinner.adapter = adapter
        }

        binding.apply {
            dateTextView.setOnClickListener {
                datePicker.showDatePicker(
                    childFragmentManager = fragmentManager,
                    textView = dateTextView,
                    preventFutureDates = true
                )
            }

            continueButton.setOnClickListener {
                val filter = Filter(
                    branch = branchesSpinner.selectedItem as Branch,
                    date = dateTextView.text.toString(),
                    paymentCompleted = paymentCompletedCheckBox.isChecked,
                    paymentUncompleted = paymentUncompletedCheckBox.isChecked
                )
                communicator.applyFilter(filter)
                dismiss()
            }
            cancelButton.setOnClickListener {
                communicator.onDismiss()
                dismiss()
            }
        }
    }

    interface Communicator {
        fun applyFilter(filter: Filter)
        fun onDismiss() {}
    }

    data class Filter(
        val branch: Branch? = null,
        val date: String? = null,
        val paymentCompleted: Boolean? = null,
        val paymentUncompleted: Boolean? = null
    )
}