package com.codeIn.myCash.ui.authentication.log_in.forgot_password

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentForgotPasswordBinding
import com.codeIn.myCash.ui.showError
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.features.data.AuthType
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {


    private val viewModel: ForgotPasswordViewModel by viewModels()
    private val infoDialog: InfoDialog = InfoDialog()

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        viewModel.apply {
            viewModel.generalResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is AuthenticationState.Loading -> handleLoading()
                    is AuthenticationState.Idle -> handleIdle()
                    is AuthenticationState.StateError -> handleError(response.message.toString())
                    is AuthenticationState.Success -> handleSuccess()
                    is AuthenticationState.InputError ->handleInputError(response.inputError)
                    is AuthenticationState.ServerError -> handleError(
                        response.error.getErrorMessage(requireContext())
                    )

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
        if ((findNavController().currentDestination?.id
                ?: 0) == R.id.navigationForgotPasswordFragment
        ) {
            binding.apply {
                val email = emailEditText.text.toString()
                val phone = phoneLayout.phoneNumberEditText.text.toString()
                when {
                    emailRadioButton.isChecked -> {
                        findNavController().navigate(
                            com.codeIn.myCash.ui.authentication.log_in.forgot_password.ForgotPasswordFragmentDirections.actionNavigationForgotPasswordFragmentToNavigationOtpVerificationFragment(
                                "", email
                            )
                        )
                    }

                    phoneRadioButton.isChecked -> {
                        findNavController().navigate(
                            com.codeIn.myCash.ui.authentication.log_in.forgot_password.ForgotPasswordFragmentDirections.actionNavigationForgotPasswordFragmentToNavigationOtpVerificationFragment(
                                phone, ""
                            )
                        )
                    }

                    else -> Snackbar.make(
                        binding.root,
                        getString(R.string.choose_one_method),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        viewModel.clearViewState()
    }

    private fun handleInputError(invalidInput: InvalidInput) {
        binding.loadingLayout.root.gone()
        when (invalidInput) {
            InvalidInput.PHONE_SAUDI -> {
                binding.phoneLayout.phoneNumberEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.phone_number_error_message),
                    isSuccess = false
                )
            }

            InvalidInput.EMAIL -> {
                binding.emailEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.email_error_message),
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
            else -> {
                // do nothing
            }
        }
    }


    /**
     * [OnClickListener] for the next button.
     * if the user choose the email method then navigate with empty phone number
     * if the user choose the phone method then navigate with the phone number
     */
    private val nextButtonOnClickListener = OnClickListener {
        binding.apply {
            when {
                emailRadioButton.isChecked -> {
                    viewModel.forgotPassword(key = emailEditText.text.toString(), AuthType.EMAIL.value.toString())
                }

                phoneRadioButton.isChecked -> {
                    viewModel.forgotPassword(key = phoneLayout.phoneNumberEditText.text.toString(), AuthType.PHONE.value.toString())
                }

                else -> Snackbar.make(
                    binding.root,
                    getString(R.string.choose_one_method), Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    /**
     * initialize the views.
     */
    private fun initViews(view: View) {
        val layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.apply {
            topLayout.layoutTransition = layoutTransition
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            //set phone radio button on checked change listener to show the phone layout and hide the email layout
            phoneRadioButton.setOnCheckedChangeListener { _, _ ->
                if (phoneRadioButton.isChecked) {
                    phoneLayout.root.visibility = View.VISIBLE
                    emailRadioButton.isChecked = false
                } else {
                    phoneLayout.root.visibility = View.GONE
                    hideKeyboard(requireContext(), view)
                }
            }

            //set email radio button on checked change listener to show the email layout and hide the phone layout
            emailRadioButton.setOnCheckedChangeListener { _, _ ->
                if (emailRadioButton.isChecked) {
                    emailEditText.visibility = View.VISIBLE
                    phoneRadioButton.isChecked = false
                } else {
                    emailEditText.visibility = View.GONE
                    //hide the keyboard to prevent typing in the email edit text when it is hidden if it has the keyboard focus
                    hideKeyboard(requireContext(), view)
                }
            }
            nextButton.setOnClickListener(nextButtonOnClickListener)
        }
    }
}