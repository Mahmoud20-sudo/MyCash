package com.codeIn.myCash.ui.options.branches.branches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.AppConstants
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.getQueryTextChangeStateFlow
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentBranchesBinding
import com.codeIn.myCash.ui.options.branches.Branch
import com.codeIn.myCash.utilities.bottom_sheets.WarningBottomSheet
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.ui.isSame
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class BranchesFragment : Fragment() {

    private val viewModel: BranchesViewModel by viewModels()

    private var _binding: FragmentBranchesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBranchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = BranchesAdapter(branchesCommunicator, requireContext())

        binding.apply {

            backArrow.setOnClickListener {
                activity?.onBackPressed()
            }

            deleteButton.setOnClickListener {
                deleteAllBranches()
            }

            addBranch.setOnClickListener {
                findNavController().navigate(com.codeIn.myCash.ui.options.branches.branches.BranchesFragmentDirections.actionNavigationBranchesFragmentToNavigationNewBranchFragment())
            }

            refreshBtn.setOnClickListener {
                viewModel.getBranches()
            }

            searchView.getQueryTextChangeStateFlow()
                .debounce(AppConstants.DELAY_TIME_SEARCH)
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .onEach { query ->
                    if (!(viewModel.searchText.value isSame query)) {
                        viewModel.updateSearchText(query)
                    }
                }.launchIn(viewModel.viewModelScope)

            branchesRecyclerView.adapter = adapter
        }

        //observe the branches list
        viewModel.apply {
            branchesList.collectOnLifecycle(
                lifecycleScope,
                viewLifecycleOwner
            ) { branchesList ->
                adapter.submitList(branchesList)
                binding.apply {
//                    deleteButton.isVisible = branchesList.isNotEmpty()
                }
            }

            getBranchesResultState.collectOnLifecycle(
                lifecycleScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is BranchesState.SuccessRetrieveBranches -> {
                        binding.emptyStateIv.isVisible = response.data?.data?.data?.isEmpty() == true
                        viewModel.clearState()
                    }
                    is BranchesState.Loading -> handleLoading()
                    is BranchesState.Idle -> handleIdle()
                    is BranchesState.ServerError -> handleError(
                        response.error.getErrorMessage(
                            requireContext()
                        )
                    )
                    else -> {}
                }
            }

            deleteBranchesResultState.collectOnLifecycle(
                lifecycleScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is BranchesState.SuccessDeleteAllBranches -> { viewModel.getBranches() }
                    is BranchesState.SuccessDeleteSingleBranch -> { viewModel.getBranches() }
                    is BranchesState.Loading -> {}
                    is BranchesState.Idle -> handleIdle()
                    is BranchesState.ServerError -> handleError(
                        response.error.getErrorMessage(
                            requireContext()
                        )
                    )
                    else -> {}
                }
            }

            searchText.observe(viewLifecycleOwner) {
                viewModel.getBranches()
            }
        }
    }

    /**
     * Communicator used to communicate with the adapter
     */
    private val branchesCommunicator = object : BranchesAdapter.Communicator {

        override fun onLongClick(branch: BranchesDTO.Data.Data) {
            showBranchOptionsDialog(branch)
        }

        override fun branchDetailsClick(branch: BranchesDTO.Data.Data) {
            val action =
                com.codeIn.myCash.ui.options.branches.branches.BranchesFragmentDirections.actionNavigationBranchesFragmentToBranchDetailsFragment(
                    branch.id!!,
                    branch.phone ?: "",
                    branch.city ?: "",
                    branch.status!!,
                    branch.address ?: ""
                )
            findNavController().navigate(action)
        }
    }

    //we keep a reference to the dialog so that we can dismiss it user clicks options multiple times
    private var branchOptionsDialog: BranchOptionsDialog? = null

    /**
     * Shows the options dialog for a branch (edit, delete, activate)
     * @param branch [Branch] the branch for which the options are shown
     */
    private fun showBranchOptionsDialog(branch: BranchesDTO.Data.Data) {
        branchOptionsDialog?.dismiss()
        branchOptionsDialog =
            BranchOptionsDialog(requireContext(), branch, branchOptionsCommunicator)
        branchOptionsDialog?.show()
    }


    /**
     * Communicator used to communicate with the dialog
     */
    private val branchOptionsCommunicator = object : BranchOptionsDialog.Communicator {
        override fun edit(branch: BranchesDTO.Data.Data) {
            val action =
                com.codeIn.myCash.ui.options.branches.branches.BranchesFragmentDirections.actionNavigationBranchesFragmentToEditBranchFragment(
                    branch.id!!,
                    branch.name!!,
                    branch.isMain.toString().toInt(),
                    branch.phone?.toInt() ?: 0,
                    branch.city ?: "",
                    branch.address ?: "",
                    branch.additionalInfo ?: ""
                )
            findNavController().navigate(action)
        }

        override fun delete(branch: BranchesDTO.Data.Data) {
            deleteBranch(branch)
        }

        override fun activate(branch: BranchesDTO.Data.Data, isChecked: Boolean) {
            val status = if (isChecked)
                1
            else
                0
            branchOptionsDialog?.dismiss()
            viewModel.updateBranch(
                branch.name ?: "",
                branch.isMain ?: 0,
                branch.address ?: "",
                branch.id ?: 0,
                status,
                branch.additionalInfo ?: "",
                branch.city ?: "",
                branch.phone ?: "0"
            )
        }
    }


    //the warning bottom sheet to show when the user clicks on the delete all branches button
    //we need to keep a reference to it to dismiss it when the user clicks multiple times.
    //if we don't dismiss it, it will be shown multiple times.
    private var warningBottomSheet: WarningBottomSheet? = null

    /**
     * function to show a warning bottom sheet to the user when he clicks on the delete all branches button.
     * @return [Unit]
     */
    private fun deleteAllBranches() {
        warningBottomSheet?.dismiss()
        warningBottomSheet = WarningBottomSheet(
            title = getString(R.string.delete_all_branches),
            message = getString(R.string.delete_all_branches_message),
            communicator = deleteAllBranchesCommunicator

        )
        warningBottomSheet?.show(childFragmentManager, WarningBottomSheet.TAG)
    }

    private fun deleteBranch(branch: BranchesDTO.Data.Data) {
        warningBottomSheet?.dismiss()
        warningBottomSheet = WarningBottomSheet(
            title = "Delete ${branch.name}",
            message = "Are you sure you want to delete ${branch.name}?",
            communicator = object : WarningBottomSheet.Communicator {
                override fun confirm() {
                    if (branch.isMain == 1)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.please_select_another_branch_before_delete_this_branch),
                            Toast.LENGTH_LONG
                        ).show()
                    else
                        viewModel.deleteBranches(branch.id)
                }
            }
        )
        warningBottomSheet?.show(childFragmentManager, WarningBottomSheet.TAG)
    }


    /**
     * WarningBottomSheet communicator to handle the user clicks on the warning bottom sheet buttons.
     * @see WarningBottomSheet
     */
    private val deleteAllBranchesCommunicator = object : WarningBottomSheet.Communicator {
        override fun confirm() {
            viewModel.deleteAllBranches(all = 1)
        }
    }

    private fun handleLoading() = binding.animationView.visible()

    private fun handleIdle() = binding.animationView.gone()

    private fun handleError(message: String) {
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }
}