package com.codeIn.myCash.ui.options.branches.edit_branch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentBranchesBinding
import com.codeIn.myCash.databinding.FragmentEditBranchBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBranchFragment : Fragment() {
    private var _binding: FragmentEditBranchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditBranchViewModel by viewModels()
    private var id: Int? = null
    private var name: String? = null
    private var main: Int? = null
    private var phone: Int? = null
    private var city: String? = null
    private var address: String? = null
    private var extraInfo: String? = null

    private fun handleLoading() = binding.animationView.visible()

    private fun handleIdle() = binding.animationView.gone()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = com.codeIn.myCash.ui.options.branches.edit_branch.EditBranchFragmentArgs.fromBundle(
            requireArguments()
        ).id
        name = com.codeIn.myCash.ui.options.branches.edit_branch.EditBranchFragmentArgs.fromBundle(
            requireArguments()
        ).name
        main = com.codeIn.myCash.ui.options.branches.edit_branch.EditBranchFragmentArgs.fromBundle(
            requireArguments()
        ).main
        phone = com.codeIn.myCash.ui.options.branches.edit_branch.EditBranchFragmentArgs.fromBundle(
            requireArguments()
        ).phone
        city = com.codeIn.myCash.ui.options.branches.edit_branch.EditBranchFragmentArgs.fromBundle(
            requireArguments()
        ).city
        address =
            com.codeIn.myCash.ui.options.branches.edit_branch.EditBranchFragmentArgs.fromBundle(
                requireArguments()
            ).address
        extraInfo =
            com.codeIn.myCash.ui.options.branches.edit_branch.EditBranchFragmentArgs.fromBundle(
                requireArguments()
            ).extraInfo

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBranchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
    }

    private fun initialization() {
        initListeners()

        viewModel.updateBranchResponse.collectOnLifecycle(
            lifecycleScope,
            viewLifecycleOwner
        ) { response ->
            when (response) {
                is BranchesState.SuccessUpdateBranch -> {
                    handleMessage(
                        requireContext().getString(R.string.success_message), isSuccess = true
                    )
                }

                is BranchesState.Loading -> {  handleLoading() }
                is BranchesState.Idle -> { handleIdle() }
                is BranchesState.ServerError -> handleMessage(
                    response.error.getErrorMessage(
                        requireContext()
                    )

                )

                else -> {}
            }
        }
    }

    private fun handleMessage(message: String, isSuccess: Boolean = false) {
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = isSuccess
        )
//        findNavController().popBackStack()

        viewModel.clearState()
    }

    private fun initListeners() {
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }

            nameEditText.setText(name)

            activeBranch.isChecked = main == 1
            phoneLayout.phoneNumberEditText.setText(phone?.toString())
            cityEditText.setText(city)
            addressEditText.setText(address)
            extraInfoEditText.setText(extraInfo)

            editButton.setOnClickListener {
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
                    Snackbar.make(binding.root, "Please enter an address", Snackbar.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else if (phoneText.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        "Please enter a phone number",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (city.isEmpty()) {
                    Snackbar.make(binding.root, "Please enter a city", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (extraInfo.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        "Please enter extra information",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener

                } else if (main.isEmpty()) {
                    Snackbar.make(binding.root, "Please enter a status", Snackbar.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else if (!validatePhoneNumber(phoneText)) {
                    Snackbar.make(
                        binding.root,
                        "Please enter a valid phone number (9 digits starting with 5)",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.updateBranch(
                    name = name,
                    isMain = main.toInt(),
                    address = address,
                    branchID = id!!,
                    status = 1, // Assuming you have a status value here
                    additionalInfo = extraInfo,
                    city = city,
                    phone = phoneText
                )
            }
        }
    }

    private fun validatePhoneNumber(phone: String): Boolean {
        val phonePattern = Regex("^5\\d{8}$")
        return phone.matches(phonePattern)
    }
}