package com.codeIn.myCash.ui.home.clients_vendors.update_client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentEditClientBinding
import com.codeIn.myCash.ui.authentication.showError
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpdateClientFragment : Fragment() {

    private val viewModel: UpdateClientViewModel by viewModels()
    private var _binding: FragmentEditClientBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback() {
//            findNavController().previousBackStackEntry?.savedStateHandle?.set("client", viewModel.client.value!!)
//            findNavController().popBackStack()
        }
        //set the click listeners for the back arrow and the cancel button to pop the back stack
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("client", viewModel.client.value!!)
                findNavController().popBackStack()
            }
            editButton.setOnClickListener {
                hideKeyboard(requireContext(), view)
                viewModel.updateClient(name = nameEditText.text.toString() ,
                    phone = phoneLayout.phoneNumberEditText.text.toString() ,
                    email = emailEditText.text.toString() ,
                    address = addressEditText.text.toString() ,
                    extra = extraInfoEditText.text.toString() ,
                    commercialRecordNo = commercialRegistrationNoEditText.text.toString() ,
                    taxNo = taxNumberEditText.text.toString() ,
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
                    is ClientState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ClientState.StateError -> handleError(response.message.toString())
                    is ClientState.SuccessShowSingleClient -> {
                        updateCurrentClient(response.data)
                        handleSuccess()
                    }
                    is ClientState.InputError -> handleInputError(response.inputError)
                    is ClientState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }


            client.collectOnLifecycle(viewModelScope , viewLifecycleOwner){client->
                handleSuccessSingleClient(client)
            }

        }

    }


    private fun handleSuccessSingleClient(client : ClientModel?){
        viewModel.clearState()
        binding.apply {
            nameEditText.setText(client?.name)
            phoneLayout.phoneNumberEditText.setText(client?.phone)
            commercialRegistrationNoEditText.setText(client?.commercialRecord)
            taxNumberEditText.setText(client?.taxRecord)
            addressEditText.setText(client?.address)
            emailEditText.setText(client?.email)
            extraInfoEditText.setText(client?.notes)
        }
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }

    private fun handleSuccess() {
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().previousBackStackEntry?.savedStateHandle?.set("client", viewModel.client.value)
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

            InvalidInput.EMAIL ->{
                binding.emailEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.email_error_message),
                    isSuccess = false
                )
            }
            InvalidInput.TAX_RECORD ->{
                binding.taxNumberEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_tax_record),
                    isSuccess = false
                )
            }
            InvalidInput.COMMERCIAL_RECORD ->{
                binding.taxNumberEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_commercial_record),
                    isSuccess = false
                )
            }
            InvalidInput.EMPTY -> {
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_fill_all_the_fields),
                    isSuccess = false
                )
            }

            else -> {}
        }
    }



}