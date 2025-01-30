package com.codeIn.myCash.ui.options.users.edit_user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentEditUsersBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.features.user.data.users.model.response.UsersResponse
import com.codeIn.myCash.ui.options.users.add_user.SearchBranchAutoCompleteTextViewAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditUsersFragment : Fragment() {
    companion object {
        const val EDIT_USER_KEY = "edit_user_key"
    }

    private var _binding: FragmentEditUsersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditUserViewModel by viewModels()
    private var employeeID: Int? = null
    private var name:String? = null
    private var email:String? = null
    private var phone:String? = null
    private var note:String? = null
    private var authority :Int? =null
    private var status :Int? =null
    private var branchId :String? ="0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        employeeID = EditUsersFragmentArgs.fromBundle(requireArguments()).employeeId
        name = EditUsersFragmentArgs.fromBundle(requireArguments()).name
        email = EditUsersFragmentArgs.fromBundle(requireArguments()).email
        phone = EditUsersFragmentArgs.fromBundle(requireArguments()).phone
        note = EditUsersFragmentArgs.fromBundle(requireArguments()).city
        status = EditUsersFragmentArgs.fromBundle(requireArguments()).status
        authority = EditUsersFragmentArgs.fromBundle(requireArguments()).authority
        branchId = EditUsersFragmentArgs.fromBundle(requireArguments()).branchId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditUsersBinding.inflate(inflater, container, false)
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
            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }

            nameEditText.setText(name)
            emailAddressEditText.setText(email)
            activeUser.isChecked = status == 1
            if (authority == 1){
                admin.isChecked = true
            }
            else {
                employee.isChecked = true
            }
            noteEditText.setText(note)
            phoneLayout.phoneNumberEditText.setText(phone!!)


            editButton.setOnClickListener {

                val statusText = if (binding.activeUser.isChecked)
                    "1"
                else
                    "2"

                val typeText = if (binding.admin.isChecked)
                    "1"
                else
                    "2"
                val branchId = viewModel.selectedBranch.value?.id?:0

                if (binding.nameEditText.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, getString(R.string.name), Snackbar.LENGTH_SHORT).show()

                    return@setOnClickListener
                } else if (binding.phoneLayout.phoneNumberEditText.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, getString(R.string.phone_number), Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
//                else if (binding.noteEditText.toString().isEmpty()) {
//                    Snackbar.make(binding.root, getString(R.string.extra_info), Snackbar.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
                else if (email!!.isEmpty()) {
                    Snackbar.make(binding.root, getString(R.string.email_address), Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (statusText.isEmpty() || statusText !in listOf("1", "2")) {
                    Snackbar.make(binding.root, "Please enter either 1 or 2 for authority", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else if (branchId == 0 ) {
                    Snackbar.make(binding.root, getString(R.string.branch_name), Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val phoneText = binding.phoneLayout.phoneNumberEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val note = binding.noteEditText.text.toString()

                val phone: String?
                val status: Int
                try {
                    phone = phoneText
                    status = statusText.toInt()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Invalid input for phone or authority", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Validate email
                val email = binding.emailAddressEditText.text.toString()
                val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
                if (!email.matches(emailPattern)) {
                    Snackbar.make(binding.root, getString(R.string.email_error_message), Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                // Validate phone number
                val isValidPhone = validatePhoneNumber(phoneText)
                if (!isValidPhone) {
                    Snackbar.make(binding.root, getString(R.string.please_enter_a_valid_saudi_phone_number), Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                viewModel.editUser(
                    name = binding.nameEditText.text.toString(),
                    email = email,
                    phone = phone,
                    password = password,
                    note = note,
                    employeeId = employeeID!! ,
                    type = typeText ,
                    branchId = (branchId).toString()
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.editUsers.collect { response ->
                        when (response) {
                            is UsersResponse.Success -> {
                                findNavController().popBackStack()
                            }
                            else -> {
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }

            branchName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    viewModel.updateSelectedBranch(null)
                }
                override fun afterTextChanged(editable: Editable) {}
            })

        }

        viewModel.apply {
            branchesList.observe(viewLifecycleOwner ){ dataList ->

                if (dataList.isNotEmpty()){

                    dataList.filter { it.id == (branchId ?: "0").toInt() }.forEach { data ->
                        binding.branchName.setText(data.name)
                        updateSelectedBranch(data)
                    }

                    binding.branchName.setDropDownBackgroundDrawable(binding.root.context.resources.getDrawable(R.drawable.autocomplete_dropdown))

                    binding.branchName.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                        val autoCompleteText = adapterView.getItemAtPosition(i) as BranchesDTO.Data.Data
                        binding.branchName.setText(autoCompleteText.name)
                        updateSelectedBranch(autoCompleteText)
                    }

                    val searchBranchAutoCompleteTextViewAdapter =  SearchBranchAutoCompleteTextViewAdapter(binding.root.context , dataList)
                    binding.branchName.setAdapter(searchBranchAutoCompleteTextViewAdapter)
                }
            }
        }
    }
    private fun validatePhoneNumber(phone: String): Boolean {
        val phonePattern = Regex("^5\\d{8}$")
        return phone.matches(phonePattern)
    }

}