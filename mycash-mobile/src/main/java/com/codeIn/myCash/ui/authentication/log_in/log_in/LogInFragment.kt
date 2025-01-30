package com.codeIn.myCash.ui.authentication.log_in.log_in

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.codeIn.common.data.InvalidInput.EMPTY
import com.codeIn.common.data.InvalidInput.PASSWORD
import com.codeIn.common.data.InvalidInput.PHONE_SAUDI
import com.codeIn.common.data.Languages
import com.codeIn.common.data.RegisterType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentLogInBinding
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.Idle
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.InputError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.Loading
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.ServerError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.StateError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.Success
import com.codeIn.myCash.ui.authentication.AuthenticationActivity
import com.codeIn.myCash.ui.elze
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.ui.showError
import com.codeIn.myCash.ui.then
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private val viewModel: LogInViewModel by viewModels()
    private val infoDialog: InfoDialog = InfoDialog()

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var prefs: SharedPrefsModule

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            freeTryButton.setOnClickListener {
                navigateTo(
                    LogInFragmentDirections.actionNavigationLogInFragmentToNavigationCreateAccountFragment(
                        RegisterType.FREE.value
                    )
                )
            }

            forgotPasswordTextView.setOnClickListener {
                navigateTo(LogInFragmentDirections.actionNavigationLogInFragmentToNavigationForgotPasswordFragment())
            }

            registerTextView.setOnClickListener {
                navigateTo(
                    LogInFragmentDirections.actionNavigationLogInFragmentToNavigationCreateAccountFragment(
                        RegisterType.PAID.value
                    )
                )
            }

            logInButton.setOnClickListener {
                hideKeyboard(requireContext(), view)
                viewModel.login(
                    phone = phoneLayout.phoneNumberEditText.text.toString(),
                    password = passwordEditText.text.toString()
                )
            }
            rememberMe.setOnClickListener {
                if (rememberMe.isChecked) {
                    viewModel.saveCache(
                        phoneLayout.phoneNumberEditText.text.toString(),
                        passwordEditText.text.toString()
                    )
                } else {
                    prefs.putValue(Constants.isRememberMe(), "0")
                    prefs.putValue(Constants.getPhone(), "")
                    prefs.putValue(Constants.getPassword(), "")
                }

            }
            if (prefs.getValue(Constants.isRememberMe()).equals("1")) {
                phoneLayout.phoneNumberEditText.setText(
                    prefs.getValue(Constants.getPhone())
                )
                passwordEditText.setText(
                    prefs.getValue(Constants.getPassword())
                )
                rememberMe.isChecked = true
            }

            phoneLayout.phoneNumberEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    if (prefs.getValue(Constants.isRememberMe()).equals("1")) {

                        prefs.putValue(
                            Constants.getPhone(),
                            s.toString()
                        )
                    }
                }
            })
            passwordEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    if (prefs.getValue(Constants.isRememberMe()).equals("1")) {

                        prefs.putValue(Constants.getPassword(), s.toString())
                    }
                }
            })


        }
        setLanguagesViews()
        viewModel.apply {
            authResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is Loading -> handleLoading()
                    is Idle -> handleIdle()
                    is StateError -> handleError(response.message.toString())
                    is Success -> {
                        response.data.also { user->
                            user?.status.apply {
                                then {
                                    user?.paymentStatus.apply {
                                        then {
                                            user?.isCompleteAccountInfo.apply {
                                                then {
                                                    handleSuccess() }
                                                elze {
                                                    navigateTo(LogInFragmentDirections.actionNavigationLogInFragmentToCompleteInfoFragment()) }
                                            }
                                        }
                                        elze {
                                            navigateTo(LogInFragmentDirections.actionNavigationLogInFragmentToNavigationSubscriptionsFragment()) }
                                    }

                                }
                                elze {
                                    navigateTo(LogInFragmentDirections.actionNavigationLogInFragmentToOtpVerificationSignUpFragment("${user?.phone}")) }
                            }
                        }
                    }
                    is InputError -> handleInputError(response.inputError)
                    is ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }
        }
    }

    private fun setLanguagesViews() {
        //get the primary and secondary languages views based on the device language
        val primaryLanguagesView: View
        val secondaryLanguagesView: View

        val lang = prefs.getLanguage().ifEmpty {
            prefs.putValue(Constants.LANG, Locale.getDefault().language)
        }
        if (lang == Languages.AR.value) {
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

    private fun handleLoading() = binding.animationView.visible()

    private fun handleIdle() = binding.animationView.gone()

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
            requireContext(),
            getString(R.string.success_message),
            isSuccess = true
        )
        val intent = Intent(activity , MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun handleInputError(invalidInput: InvalidInput) {
        viewModel.clearState()
        when (invalidInput) {
            PHONE_SAUDI -> {
                binding.phoneLayout.phoneNumberEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_a_valid_saudi_phone_number),
                    isSuccess = false
                )
            }

            PASSWORD -> {
                binding.passwordTextInputLayout.showError(binding.passwordEditText)
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.password_is_not_correct),
                    isSuccess = false
                )
            }

            EMPTY -> {
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_fill_all_the_fields),
                    isSuccess = false
                )
            }

            else -> {}
        }
    }


    /**
     * this function is used to navigate to another fragment if the current fragment is the log in fragment
     * @param navDirections [NavDirections] the direction to navigate to.
     * @return [Unit]
     * @see LogInFragmentDirections
     */
    private fun navigateTo(navDirections: NavDirections) {
        //make sure that the current fragment is the log in fragment before navigating.
        if ((findNavController().currentDestination?.id
                ?: 0) == R.id.navigationLogInFragment
        )
            findNavController().navigate(navDirections)
    }
}