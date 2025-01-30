package com.codeIn.myCash.ui.options.users.add_user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentAddUserBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.user.data.users.model.response.CreateUserResponse
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddUserFragment : Fragment() {
    companion object {
        const val NEW_USER_KEY = "new_user_key"
    }

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }
            addButton.setOnClickListener {
                val name = binding.nameEditText.text.toString()
                val email = binding.emailAddressEditText.text.toString()
                val phoneText = binding.phoneLayout.phoneNumberEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val statusText = if (binding.activeUser.isChecked)
                    "1"
                else
                    "2"
                val typeText = if (binding.admin.isChecked)
                    "1"
                else
                    "2"
                val branchId = viewModel.selectedBranch.value?.id ?: 0

                if (binding.nameEditText.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, getString(R.string.name), Snackbar.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener


                } else if (binding.phoneLayout.phoneNumberEditText.text.toString().isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.phone_number),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (binding.passwordEditText.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, getString(R.string.password), Snackbar.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
//                else if (binding.extraInfoEditText.text.toString().isEmpty()) {
//                    Snackbar.make(binding.root, getString(R.string.extra_info), Snackbar.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
                else if (email.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.email_address),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (statusText.isEmpty() || statusText !in listOf(
                        "1",
                        "2"
                    )
                ) { // Check if statusText is empty or not 1 or 2
                    Snackbar.make(
                        binding.root,
                        "Please enter either 1 or 2 for authority",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (branchId == 0) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.branch_name),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val phone: String?
                val status: Int
                try {
                    phone = phoneText
                    status = statusText.toInt()
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        requireContext(),
                        "Invalid input for phone or authority",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // Validate email
                val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
                if (!email.matches(emailPattern)) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.email_error_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // Validate phone number
                val isValidPhone = validatePhoneNumber(phoneText)
                if (!isValidPhone) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.please_enter_a_valid_saudi_phone_number),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.createUser(name, email, phone, password, status, typeText)
            }

            branchName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i2: Int,
                    i3: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    viewModel.updateSelectedBranch(null)
                }

                override fun afterTextChanged(editable: Editable) {}
            })
        }

        viewModel.apply {
            branchesList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    binding.branchName.setDropDownBackgroundDrawable(
                        ResourcesCompat.getDrawable( requireContext().resources,R.drawable.autocomplete_dropdown, null)
                    )

                    binding.branchName.onItemClickListener =
                        AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                            val autoCompleteText =
                                adapterView.getItemAtPosition(i) as BranchesDTO.Data.Data
                            binding.branchName.setText(autoCompleteText.name)
                            updateSelectedBranch(autoCompleteText)
                        }

                    val searchBranchAutoCompleteTextViewAdapter =
                        SearchBranchAutoCompleteTextViewAdapter(binding.root.context, it)
                    binding.branchName.setAdapter(searchBranchAutoCompleteTextViewAdapter)
                }
            }

            usersResponse.collectOnLifecycle(lifecycleScope, viewLifecycleOwner) { response ->
                when (response) {
                    is UsersState.SuccessCreateUser -> {
                        val userId = response.data?.data?.id
                        if (userId != null) {
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                NEW_USER_KEY, userId
                            )
                            findNavController().popBackStack()
                        } else {
                            Log.e("AddUserFragment", "User ID is null")
                        }
                        viewModel.clearState()
                    }
                    // Handle other response states if needed
                    else -> {
                        // Handle other response states if needed
                        viewModel.clearState()
                    }
                }
            }

            branchesResponse.collectOnLifecycle(lifecycleScope, viewLifecycleOwner) { response ->
                when (response) {
                    is BranchesState.SuccessRetrieveBranches -> {}
                    // Handle other response states if needed
                    else -> {
                        // Handle other response states if needed
                        viewModel.clearState()
                    }
                }
            }
        }

    }


    private fun validatePhoneNumber(phone: String): Boolean {
        val phonePattern = Regex("^5\\d{8}$")
        return phone.matches(phonePattern)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}