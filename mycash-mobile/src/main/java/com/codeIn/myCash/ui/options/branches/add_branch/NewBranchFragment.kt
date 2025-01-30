package com.codeIn.myCash.ui.options.branches.add_branch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.databinding.FragmentNewBranchBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesResponse
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class NewBranchFragment : Fragment() {
    companion object {
        const val NEW_BRANCH_KEY = "new_branch_key"
    }

    private var _binding: FragmentNewBranchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: NewBranchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewBranchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val address = binding.addressEditText.text.toString()
            val city = binding.cityEditText.text.toString()
            val extraInfo = binding.extraInfoEditText.text.toString()

            val main = if (binding.activeBranch.isChecked)
                "1"
            else
                "2"

            val phoneText = binding.phoneLayout.phoneNumberEditText.text.toString()

            if (name.isEmpty()) {
                Snackbar.make(binding.root, "Please enter a name", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (address.isEmpty()) {
                Snackbar.make(binding.root, "Please enter an address", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (phoneText.isEmpty()) {
                Snackbar.make(binding.root, "Please enter a phone number", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else if (city.isEmpty()) {
                Snackbar.make(binding.root, "Please enter a city", Snackbar.LENGTH_SHORT).show()

                return@setOnClickListener
            } else if (extraInfo.isEmpty()) {
                Snackbar.make(binding.root, "Please enter extra information", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else if (main.isEmpty()) {
                Snackbar.make(binding.root, "Please enter a status", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener

            } else if (!validatePhoneNumber(phoneText)) {
                Snackbar.make(
                    binding.root,
                    "Please enter a valid phone number (9 digits starting with 5)",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.createBranch(
                name = name,
                address = address,
                isMain = main.toInt(),
                employeeId = 41, // Assuming you have a value here
                status = 1, // Assuming you have a status value here
                city = city,
                additionalInfo = extraInfo,
                phone = phoneText
            )

            viewModel.branchesResponse.collectOnLifecycle(
                lifecycleScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is BranchesState.SuccessCreateBranch -> {
                        val branch = response.data?.`data`
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            NEW_BRANCH_KEY, branch
                        )
                        findNavController().popBackStack()
                    }
                    // Handle other response states if needed
                    else -> {
                        // Handle other response states if needed
                    }
                }
            }
        }
    }

    private fun validatePhoneNumber(phone: String): Boolean {
        val phonePattern = Regex("^5\\d{8}$")
        return phone.matches(phonePattern)
    }

}