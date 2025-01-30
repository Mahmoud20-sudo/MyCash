package com.codeIn.myCash.ui.options.profile.otp_update

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.RegisterType
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.disable
import com.codeIn.common.util.enable
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentOtpUpdateProfileBinding
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.ui.options.profile.ProfileViewModel
import com.codeIn.myCash.utilities.MyAnimator
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import dagger.hilt.android.AndroidEntryPoint
import `in`.aabhasjindal.otptextview.OTPListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class OtpUpdatePhoneFragment : Fragment() {
    private var _binding: FragmentOtpUpdateProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OtpUpdateProfileViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private var phone :String? =null
    private var oldPhone :String? =null
    private var email :String? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phone = com.codeIn.myCash.ui.options.profile.otp_update.OtpUpdatePhoneFragmentArgs.fromBundle(
            requireArguments()
        ).newPhone
        oldPhone = com.codeIn.myCash.ui.options.profile.otp_update.OtpUpdatePhoneFragmentArgs.fromBundle(
            requireArguments()
        ).phone
        email = com.codeIn.myCash.ui.options.profile.otp_update.OtpUpdatePhoneFragmentArgs.fromBundle(
            requireArguments()
        ).email
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            resendOtpButton.setOnClickListener {
                viewModel.resendOtp(oldPhone, email)
            }

            if (sequenceOf(phone, email).none { it.isNullOrBlank() }) {
                profileViewModel.editCode(oldPhone!!,1,email!!)
            }

            confirmButton.setOnClickListener {
                binding.otpView.run {
                    if (this.otp.toString().isNotEmpty()) {
                        profileViewModel.phoneUpdate(phone!!, this.otp.toString().toInt())
                    } else
                        otpView.showError()
                }
            }

            binding.otpView.otpListener = otpListener
        }

        startCounter()

        viewModel.apply {
            phoneNumber.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { number ->
                binding.subTitleTextView.text =
                    getString(R.string.otp_sent_to_phone_number, number)

            }
            authResult.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { response ->
                when (response) {
                    is AuthenticationState.Loading -> handleLoading()
                    is AuthenticationState.Idle -> handleIdle()
                    is AuthenticationState.StateError -> handleError(response.message.toString())
                    is AuthenticationState.Success -> handleCheckCodeSuccess()
                    is AuthenticationState.InputError -> {}
                    is AuthenticationState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    is AuthenticationState.SuccessResendOtp -> handleResendCodeSuccess()
                    else -> {}
                }
            }
        }
        profileViewModel.apply {
            phoneResult.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { response ->
                response?.data?.let {
                    handleUpdatePhoneSuccess()
                }
                response?.message?.let{
                    handleError(message = it)
                }
            }
        }
    }

    private fun handleLoading() = binding.animationView.visible()

    private fun handleIdle() = binding.animationView.gone()

    private fun handleError(message: String) {
        viewModel.clearViewState()
        binding.otpView.showError()
        binding.otpErrorMassageTextView.text = message
        MyAnimator().animateShakeX(binding.otpErrorMassageTextView)
    }

    private fun handleUpdatePhoneSuccess() = findNavController().run {
        popBackStack(R.id.navigation_profileFragment, inclusive = true)
        navigate(R.id.navigation_profileFragment)
    }

    private fun handleCheckCodeSuccess() {
        viewModel.clearViewState()
        binding.otpView.showSuccess()
        viewModel.viewModelScope.launch {
            if (viewModel.registerType.value == RegisterType.FREE.value) {
                //We will save the authorized,,,
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
//                val intent = Intent(binding.root.context as Activity , MainActivity::class.java)
//                binding.root.context.startActivity(intent)
            } else {
                delay(500)
                if ((findNavController().currentDestination?.id
                        ?: 0) == R.id.otpUpdateProfile
                ) {
//                    findNavController().navigate(OtpVerificationSignUpFragmentDirections.actionNavigationOtpVerificationSignUpFragmentToNavigationSubscriptionsFragment())
                }
            }
        }
    }

    private fun handleResendCodeSuccess() {
        viewModel.clearViewState()
        binding.otpView.setOTP("")
        startCounter()
    }


    /**
     * This is the listener for the OTP box. It is used to listen to the user's interaction with the OTP box.
     */
    private val otpListener = object : OTPListener {
        override fun onInteractionListener() {
            // fired when user types something in the Otpbox
            binding.otpErrorMassageTextView.gone()
        }

        override fun onOTPComplete(otp: String) {
            // fired when user has entered the OTP fully.
            hideKeyboard(requireContext(), view)
//            viewModel.checkCode(otp)
        }
    }

    // This is the counter for the OTP expire time.
    private var countDownTimer: CountDownTimer? = null
    private fun startCounter() {
        binding.resendOtpButton.apply {
            disable()
            setTextColor(ContextCompat.getColor(requireContext(), R.color.mainBlack40))
        }

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(300000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                val timeText = String.format(
                    Locale.getDefault(),"%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                )

                binding.otpExpireCounterTextView.text =
                    HtmlCompat.fromHtml(
                        getString(R.string.otp_expires, timeText),
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                binding.resendOtpButton.enable()
                binding.resendOtpButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondaryColor
                    )
                )
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }
}