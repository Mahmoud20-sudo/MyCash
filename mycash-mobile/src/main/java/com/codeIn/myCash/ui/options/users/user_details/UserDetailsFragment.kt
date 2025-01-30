package com.codeIn.myCash.ui.options.users.user_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentUserDetailsBinding
import com.codeIn.myCash.ui.options.users.users.UsersViewModel
import com.codeIn.myCash.utilities.bottom_sheets.WarningBottomSheet
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private val viewModel: UserDetailsViewModel by viewModels()
    private val userViewModel: UsersViewModel by viewModels()

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private var user:  GetUsersDTO.Data.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = UserDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).user
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        binding.deleteButton.setOnClickListener {
            userViewModel.deleteUser(user)
            lifecycleScope.launch {
                delay(1000) // Delay for 1000 milliseconds (1 second)
                findNavController().popBackStack()
            }
        }
        binding.apply {
            userNumberTextView.text = id.toString()
            if (user?.type == 1)
                authorityTextView.text = getString(R.string.Admin)
            else
                authorityTextView.text = getString(R.string.Employee)
            phoneNumberTextView.text = user?.phone.toString()
            emailAddressTextView.text = user?.email
            branchTextView.text = user?.branch?.name
            addressTextView.text = user?.branch?.address
        }
        viewModel.getUsersDetails(user?.id ?: 0)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect { userList ->
                // Ensure the user list is not empty
                if (userList.isNotEmpty()) {
                    // Assuming you want to update UI with the first user in the list
                    val user = userList.first()
                    updateUserUI(user)
                } else {
                    // Handle case where userList is empty
                }
            }
        }

    }

    private fun updateUserUI(user: GetUsersDTO.Data.Data) {
        binding.apply {
            userNumberTextView.text = user.id.toString()
            phoneNumberTextView.text = user.phone
            authorityTextView.text = if (user.isMain == true) "Admin" else "User"
            emailAddressTextView.text = user.email
            branchTextView.text = user.mainBranch?.name
            addressTextView.text = user.mainBranch?.address

        }
    }

    //we keep a reference to the warning bottom sheet so that we can dismiss it user clicks delete all multiple times
    private var warningBottomSheet: WarningBottomSheet? = null
    private fun deleteUser(user: GetUsersDTO.Data.Data) {
        warningBottomSheet?.dismiss()
        warningBottomSheet = WarningBottomSheet(
            title = "Delete ${user.name}",
            message = "Are you sure you want to delete ${user.name}?",
            communicator = object : WarningBottomSheet.Communicator {
                override fun confirm() {
                    userViewModel.deleteUser(user)
                    findNavController().popBackStack()
                }
            }
        )
        warningBottomSheet?.show(childFragmentManager, WarningBottomSheet.TAG)
    }


    //we keep a reference to the dialog so that we can dismiss it user clicks options multiple times
    private var optionsDialog: UsersMoreDialog? = null
    private fun showUserOptionsDialog() {
        optionsDialog?.dismiss()
        optionsDialog = UsersMoreDialog(requireContext(), optionsCommunicator)
        optionsDialog?.show()
    }

    private val optionsCommunicator = object : UsersMoreDialog.Communicator {
        override fun showTotalSalesReport() {
            val action =
                com.codeIn.myCash.ui.options.users.user_details.UserDetailsFragmentDirections.actionNavUserDetailsFragmentToNavUserSalesReportFragment()
            findNavController().navigate(action)
        }

        override fun showUserActivities() {
            val action =
                com.codeIn.myCash.ui.options.users.user_details.UserDetailsFragmentDirections.actionNavUserDetailsFragmentToNavUserActivitiesLogsFragment()
            findNavController().navigate(action)
        }

    }
}