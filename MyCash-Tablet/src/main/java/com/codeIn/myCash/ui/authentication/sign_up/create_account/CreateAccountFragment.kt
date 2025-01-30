package com.codeIn.myCash.ui.authentication.sign_up.create_account

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.InvalidInput.EMAIL
import com.codeIn.common.data.InvalidInput.EMPTY
import com.codeIn.common.data.InvalidInput.PASSWORD
import com.codeIn.common.data.InvalidInput.PHONE_SAUDI
import com.codeIn.common.data.Languages
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.disable
import com.codeIn.common.util.enable
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentCreateAccountBinding
import com.codeIn.myCash.ui.authentication.AuthenticationActivity
import com.codeIn.myCash.ui.authentication.showError
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.Success
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.Idle
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.InputError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.Loading
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.StateError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.ServerError
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class CreateAccountFragment : Fragment() {

    private val viewModel: CreateAccountViewModel by viewModels()
    private val infoDialog: InfoDialog = InfoDialog()

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
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
            LogInTextView.setOnClickListener {
                if ((findNavController().currentDestination?.id
                        ?: 0) == R.id.navigationCreateAccountFragment
                )
                    findNavController().navigate(com.codeIn.myCash.ui.authentication.sign_up.create_account.CreateAccountFragmentDirections.actionNavigationCreateAccountFragmentToNavigationLogInFragment())
            }

            termsAndConditionsTextView.setOnClickListener {
                navigateTo(
                    com.codeIn.myCash.ui.authentication.sign_up.create_account.CreateAccountFragmentDirections.actionNavigationCreateAccountFragmentToNavigationTermsAndConditionsFragment(
                        binding.haveReadTermsCheckbox.isChecked
                    )
                )
            }

            haveReadTermsCheckbox.setOnCheckedChangeListener { _, b ->
                if (b) {
                    binding.continueButton.enable()
                } else {
                    binding.continueButton.disable()
                }
            }

            continueButton.setOnClickListener {
                val phone = binding.phoneLayout.phoneNumberEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                viewModel.registerUser(
                    phone = phone,
                    password = password,
                    email = email,
                )
            }
        }

        viewModel.apply {
            viewModel.authResult.collectOnLifecycle(
                viewModelScope, viewLifecycleOwner
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

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("isChecked")
            ?.observe(viewLifecycleOwner) { result ->
                // Do something with the result.
                binding.haveReadTermsCheckbox.isChecked = result
            }

        setLanguagesViews()
    }

    private fun setLanguagesViews() {
        val primaryLanguagesView: View
        val secondaryLanguagesView: View
        if (Locale.getDefault().language.equals(Languages.AR.value, true)) {
            primaryLanguagesView = binding.arabicTextView
            secondaryLanguagesView = binding.englishTextView
        } else {
            primaryLanguagesView = binding.englishTextView
            secondaryLanguagesView = binding.arabicTextView
        }

        //set the primary language view to be bold and have the primary color
        primaryLanguagesView.apply {
            setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
            setTypeface(binding.englishTextView.typeface, Typeface.BOLD)

        }

        //set the secondary language view to be normal and have the secondary color
        secondaryLanguagesView.apply {
            setTextColor(ContextCompat.getColor(requireContext(), R.color.mainBlack40))
            setTypeface(binding.englishTextView.typeface, Typeface.NORMAL)

        }
        //set the on click listeners for the language buttons
        binding.apply {
            englishTextView.setOnClickListener {
                (requireActivity() as AuthenticationActivity).changeLanguage(Languages.EN)
            }
            arabicTextView.setOnClickListener {
                (requireActivity() as AuthenticationActivity).changeLanguage(Languages.AR)
            }
        }
    }


    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        viewModel.clearRegisterState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
        viewModel.clearRegisterState()
    }

    private fun handleSuccess() {
        navigateTo(
            com.codeIn.myCash.ui.authentication.sign_up.create_account.CreateAccountFragmentDirections.actionNavigationCreateAccountFragmentToNavigationOtpVerificationSignUpFragment(
                binding.phoneLayout.phoneNumberEditText.text.toString(),
                viewModel.registerType.value
            )
        )
        viewModel.clearRegisterState()
    }

    private fun handleInputError(invalidInput: InvalidInput) {
        when (invalidInput) {
            PHONE_SAUDI -> {
                binding.phoneLayout.phoneNumberEditText.showError()
                CustomToaster.show(
                    context = requireContext(),
                    message = getString(R.string.phone_number_error_message),
                    isSuccess = false
                )
            }

            PASSWORD -> {
                binding.passwordTextInputLayout.showError(binding.passwordEditText)
                CustomToaster.show(
                    context = requireContext(),
                    message = getString(R.string.password_error_message),
                    isSuccess = false
                )
            }

            EMAIL -> {
                binding.emailEditText.showError()
                CustomToaster.show(
                    context = requireContext(),
                    message = getString(R.string.email_error_message),
                    isSuccess = false
                )

            }

            EMPTY -> {
                CustomToaster.show(
                    context = requireContext(),
                    message = getString(R.string.please_fill_all_the_fields),
                    isSuccess = false
                )

            }

            else -> {
                // do nothing
            }
        }
        viewModel.clearRegisterState()
    }

    /**
     * this function is used to navigate to another fragment if the current fragment is the create account fragment
     * @param navDirections [NavDirections] the direction to navigate to.
     * @return [Unit]
     * @see CreateAccountFragmentDirections
     */
    private fun navigateTo(navDirections: NavDirections) {
        //make sure that the current fragment is the create account fragment before navigating.
        if ((findNavController().currentDestination?.id
                ?: 0) == R.id.navigationCreateAccountFragment
        )
            findNavController().navigate(navDirections)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

}