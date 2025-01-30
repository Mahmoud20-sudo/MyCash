package com.codeIn.myCash.ui.options.users.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.AppConstants
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.getQueryTextChangeStateFlow
import com.codeIn.common.util.gone
import com.codeIn.common.util.isSame
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentUsersBinding
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.utilities.bottom_sheets.WarningBottomSheet
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
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


@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels()

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UsersAdapter

    private val infoDialog: InfoDialog = InfoDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UsersAdapter(usersCommunicator)
        binding.usersRecyclerView.adapter = adapter
        binding.usersRecyclerView.itemAnimator = null

        binding.apply {
            backArrow.setOnClickListener {
                activity?.onBackPressed()
            }

            addImageView.setOnClickListener {
                val action =
                    com.codeIn.myCash.ui.options.users.users.UsersFragmentDirections.actionNavUsersFragmentToNavAddUserFragment()
                findNavController().navigate(action)
            }

            deleteButton.setOnClickListener {
                deleteAllUsers()
            }

            binding.refreshBtn.setOnClickListener {
                viewModel.getRefresh()
            }

            binding.searchView.getQueryTextChangeStateFlow()
                .debounce(AppConstants.DELAY_TIME_SEARCH)
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .onEach { query ->
                    if (!(viewModel.searchText.value isSame query)) {
                        viewModel.updateSearchText(query)
                    }
                }.launchIn(viewModel.viewModelScope)
        }

        viewModel.apply {
            usersList.collectOnLifecycle(lifecycleScope, viewLifecycleOwner) {
                usersList.collect { usersList ->
                    adapter.submitList(usersList)
                    binding.apply {
                        emptyStateIv.isVisible = usersList.isEmpty()
                        deleteButton.isVisible = usersList.isNotEmpty()
                    }
                }
            }
            getUsersResultState.collectOnLifecycle(lifecycleScope, viewLifecycleOwner) { response ->
                when (response) {
                    is UsersState.SuccessRetrieveUsers -> {}
                    is UsersState.Loading -> handleLoading()
                    is UsersState.Idle -> handleIdle()
                    is UsersState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }

            deleteUsersResultState.collectOnLifecycle(
                lifecycleScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is UsersState.SuccessDeleteAllUsers -> {
                        getRefresh()
                        clearState()
                        response.data?.message?.let { handleError(it) }
                    }
                    is UsersState.SuccessDeleteSingleUser -> response.data?.message?.let { handleError(it) }
                    is UsersState.Loading -> { }
                    is UsersState.StateError -> handleError("${response.message}")
                    is UsersState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }

            searchText.observe(viewLifecycleOwner) {
                viewModel.getRefresh()
                hideKeyboard(requireContext(), view)
            }

            deleteUser.collectOnLifecycle(
                lifecycleScope,
                viewLifecycleOwner
            ) { user ->
                user?.let { adapter.deleteUser(it) }
                infoDialog.dismiss()
            }
        }

    }

    /**
     * Communicator used to communicate with the adapter
     */
    private val usersCommunicator = object : UsersAdapter.Communicator {
        override fun onLongClick(user: GetUsersDTO.Data.Data) {
            showUserOptionsDialog(user)
        }

        override fun onClick(user: GetUsersDTO.Data.Data) {
            val action = UsersFragmentDirections.actionNavUsersFragmentToNavUserDetailsFragment(user)
            findNavController().navigate(action)
        }
    }

    //we keep a reference to the dialog so that we can dismiss it user clicks options multiple times
    private var optionsDialog: UsersOptionsDialog? = null

    /**
     * function to show the user options dialog when the user clicks on the user item
     * @param user [User] the user to show the options for
     * @return [Unit]
     */
    private fun showUserOptionsDialog(user: GetUsersDTO.Data.Data) {
        optionsDialog?.dismiss()
        optionsDialog = UsersOptionsDialog(requireContext(), user, optionsCommunicator)
        optionsDialog?.show()
    }

//    private var filterDialog: UsersFilterDialog? = null
//    private fun showFilterDialog() {
//        filterDialog?.dismiss()
//        filterDialog = UsersFilterDialog(
//            context = requireContext(),
//            branches = viewModel.branchList,
//            fragmentManager = childFragmentManager,
//            communicator = filterDialogCommunicator
//        )
//        filterDialog?.show()
//    }

    private val filterDialogCommunicator = object : UsersFilterDialog.Communicator {
        override fun applyFilter(filter: UsersFilterDialog.Filter) {
            viewModel.applyFilter(filter)
        }
    }


    /**
     * Communicator used to communicate with the dialog
     */
    private val optionsCommunicator = object : UsersOptionsDialog.Communicator {
        override fun edit(user: GetUsersDTO.Data.Data) {
            val action = user?.let {
                com.codeIn.myCash.ui.options.users.users.UsersFragmentDirections.actionNavUsersFragmentToEditUsersFragment(
                    it.id ?: 0,
                    it.name ?: "",
                    it.email ?: "",
                    it.note ?: "",
                    it.phone ?: "",
                    ((it.type ?: "2") as Int),
                    ((it.status ?: "0") as Int),
                    (it.branch?.id ?: 0).toString()
                )
            }
            action?.let { findNavController().navigate(it) }
        }

        override fun delete(user: GetUsersDTO.Data.Data) {
            deleteUser(user)
        }

        override fun activate(user: GetUsersDTO.Data.Data) {
        }

        override fun share(user: GetUsersDTO.Data.Data) {
            val shareBody =
                "" + "\n" + binding.root.context.resources.getString(R.string.app_name) + "\n" + "\n" +
                        getString(R.string.user_info) + "\n" + binding.root.context.getString(R.string.name) + " : " + user.name + "\n" +
                        getString(R.string.email_address) + " : " + user.email + "\n" +
                        getString(R.string.phone_number) + " : " + user.country?.countryCode + user.phone + "\n" +
                        "  https://play.google.com/store/apps/details?id=${binding.root.context.packageName}"

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing User")

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

            context?.startActivity(Intent.createChooser(shareIntent, "Share user"))
        }
    }


    //we keep a reference to the warning bottom sheet so that we can dismiss it user clicks delete all multiple times
    private var warningBottomSheet: WarningBottomSheet? = null

    /**
     * function to show a warning bottom sheet to the user when he clicks on the delete all branches button.
     * @return [Unit]
     */
    private fun deleteAllUsers() {
        if(adapter.currentList.none { it.type != 1 }) {
            handleError(getString(R.string.non_employees_users))
            return
        }
        warningBottomSheet?.dismiss()
        warningBottomSheet = WarningBottomSheet(
            title = getString(R.string.delete_all_users),
            message = getString(R.string.delete_all_users_message),
            communicator = deleteAllUsersCommunicator
        )
        warningBottomSheet?.show(childFragmentManager, WarningBottomSheet.TAG)
    }

    private fun deleteUser(user: GetUsersDTO.Data.Data) {
        warningBottomSheet?.dismiss()
        warningBottomSheet = WarningBottomSheet(
            title = "Delete ${user.name}",
            message = "Are you sure you want to delete ${user.name}?",
            communicator = object : WarningBottomSheet.Communicator {
                override fun confirm() {
                    handleDeleteUserLoading(user)
                    viewModel.deleteUser(user)
                }
            }
        )
        warningBottomSheet?.show(childFragmentManager, WarningBottomSheet.TAG)
    }


    /**
     * WarningBottomSheet communicator to handle the user clicks on the warning bottom sheet buttons.
     * @see WarningBottomSheet
     */
    private val deleteAllUsersCommunicator = object : WarningBottomSheet.Communicator {
        override fun confirm() {
            viewModel.deleteAll()
        }
    }

    private fun handleLoading()  = binding.animationView.visible()

    private fun handleIdle() = binding.animationView.gone()

    private fun handleError(message: String) {
        infoDialog.dismiss()
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }

    private fun handleDeleteUserLoading(user: GetUsersDTO.Data.Data){
        infoDialog.show(
            context = requireContext(),
            title = getString(R.string.loading),
            message = "${getString(R.string.loading_description)} ${user.name}"
        )
    }
}