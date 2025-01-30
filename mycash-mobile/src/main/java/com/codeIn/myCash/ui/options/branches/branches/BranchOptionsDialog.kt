package com.codeIn.myCash.ui.options.branches.branches

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogBranchOptionsBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO

/**
 * Simple dialog used to show options for a branch (edit, delete,activate)
 * @param branch the branch for which the options are shown
 * @param communicator the interface used to communicate with the fragment
 * @see BranchesFragment
 */
class BranchOptionsDialog(context: Context, private val branch: BranchesDTO.Data.Data, private val communicator: Communicator) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogBranchOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogBranchOptionsBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when (branch.status == 1) {

            true -> {
                binding.activateSwitch.isChecked = true
            }
            else -> {
                binding.activateSwitch.isChecked = false
            }
        }
        binding.editTextView.setOnClickListener {
            communicator.edit(branch)
            dismiss()
        }

        binding.deleteTextView.setOnClickListener {
            communicator.delete(branch)
            dismiss()
        }

        binding.activateSwitch.setOnCheckedChangeListener { _, isChecked ->
            branch.isActive = isChecked
            communicator.activate(branch , isChecked)
        }

    }


    /**
     * Interface used to communicate with the fragment
     * @see BranchesFragment
     */
    interface Communicator {
        /**
         * Used to edit a branch
         * @param branch the branch to be edited
         */
        fun edit(branch: BranchesDTO.Data.Data)

        /**
         * Used to delete a branch
         * @param branch the branch to be deleted
         */
        fun delete(branch: BranchesDTO.Data.Data)

        /**
         * Used to activate a branch
         * @param branch the branch to be activated
         */
        fun activate(branch: BranchesDTO.Data.Data, isChecked : Boolean)
    }
}