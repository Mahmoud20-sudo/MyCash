package com.codeIn.myCash.ui.home.clients_vendors.add_client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentAddClientsBinding
import com.codeIn.myCash.features.sales.data.clients.remote.request.AddClientRequest
import com.codeIn.myCash.ui.showError
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.ui.setErrorMsg
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddClientFragment : Fragment() {

    private val viewModel: AddClientViewModel by viewModels()
    private var _binding: FragmentAddClientsBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs: SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddClientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set the click listeners for the back arrow and the cancel button to pop the back stack
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }
            addButton.setOnClickListener {
                hideKeyboard(requireContext(), view)
                viewModel.createClient(
                    AddClientRequest(
                        name = nameEditText.text,
                        phone = phoneLayout.phoneNumberEditText.text.toString(),
                        email = emailEditText.text,
                        countryId = "1",
                        address = addressEditText.text,
                        extra = extraInfoEditText.text,
                        commercialRecordNo = commercialRegistrationNoEditText.text,
                        type = ClientsSection.CLIENTS.value,
                        taxNo = taxNumberEditText.text,
                        clientId = null
                    )
                )
            }
        }

        viewModel.apply {
            clientDataResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is ClientState.Loading -> handleLoading()
                    is ClientState.Idle -> handleIdle()
                    is ClientState.StateError -> CustomToaster.show(
                        requireContext(),
                        response.message.toString(),
                        isSuccess = false
                    )

                    is ClientState.SuccessShowSingleClient -> handleSuccess()
                    is ClientState.InputError -> handleInputError(response.inputError)
                    is ClientState.ServerError -> CustomToaster.show(
                        requireContext(),
                        response.error.getErrorMessage(requireContext()),
                        isSuccess = false
                    )

                    is ClientState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(activity, IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

                    else -> {}
                }
            }
        }

    }


    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false // Assuming error messages should be shown with an error icon
        )
    }

    private fun handleSuccess() {
        viewModel.clearState()
        CustomToaster.show(
            requireContext(),
            getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().popBackStack()
    }

    private fun handleInputError(invalidInput: InvalidInput) {
        viewModel.clearState()
        when (invalidInput) {
            InvalidInput.PHONE_SAUDI -> {
                binding.phoneLayout.phoneNumberEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_a_valid_saudi_phone_number),
                    isSuccess = false
                )
            }

            InvalidInput.EMAIL ->
                binding.emailEditText.setErrorMsg(getString(R.string.email_error_message))

            InvalidInput.TAX_RECORD ->
                binding.taxNumberEditText.setErrorMsg(getString(R.string.please_enter_valid_tax_record))

            InvalidInput.COMMERCIAL_RECORD ->
                binding.commercialRegistrationNoEditText.setErrorMsg(getString(R.string.please_enter_valid_commercial_record))

            InvalidInput.EMPTY ->
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_fill_at_least_phone_and_name),
                    isSuccess = false
                )


            else -> {}
        }
    }


}