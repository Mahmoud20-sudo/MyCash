package com.codeIn.myCash.ui.authentication.log_in.reset_password

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.InvalidInput.*
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.R
import com.codeIn.myCash.ui.showError
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.databinding.FragmentResetPasswordBinding
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

    private val viewModel: ResetPasswordViewModel by viewModels()
    private val infoDialog: InfoDialog = InfoDialog()

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
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
            nextButton.setOnClickListener {
                val password = passwordEditText.text.toString()
                val confirmPassword = confirmPasswordEditText.text.toString()
                viewModel.resetPassword(password, confirmPassword)
            }

            proceedButton.setOnClickListener {
                val intent = Intent(activity , MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        viewModel.apply {
            viewModel.authResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is Loading -> handleLoading()
                    is Idle -> handleIdle()
                    is StateError -> handleError(response.message.toString())
                    is Success -> handleSuccess()
                    is InputError -> handleInputError(response.inputError)
                    is ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }
        }
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        binding.loadingLayout.root.gone()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
        viewModel.clearViewState()
    }

    private fun handleSuccess() {
        prefs.putValue(Constants.forgetToken() , "")
        binding.newPasswordLayout.gone()
        binding.successfulOperationLayout.visible()
        viewModel.clearViewState()
    }

    private fun handleInputError(invalidInput: InvalidInput) {
        binding.loadingLayout.root.gone()
        when (invalidInput) {
            PASSWORD -> {
                binding.passwordEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.password_error_message),
                    isSuccess = false
                )
            }

            CONFIRM_PASSWORD -> {
                binding.confirmPasswordEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.confirm_password_error_message),
                    isSuccess = false
                )
            }

            else -> {
                // do nothing
            }
        }
    }

}