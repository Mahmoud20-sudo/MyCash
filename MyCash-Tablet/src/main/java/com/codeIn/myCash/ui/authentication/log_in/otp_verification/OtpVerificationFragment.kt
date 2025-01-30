package com.codeIn.myCash.ui.authentication.log_in.otp_verification

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
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.disable
import com.codeIn.common.util.enable
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentOtpVerificationBinding
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.utilities.MyAnimator
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.Success
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.Idle
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.InputError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.Loading
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.ResendCodeSuccess
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.StateError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState.ServerError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import dagger.hilt.android.AndroidEntryPoint
import `in`.aabhasjindal.otptextview.OTPListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass. is used to verify the OTP sent to the user's phone number or email address.
 */
@AndroidEntryPoint
class OtpVerificationFragment : Fragment() {

    private val viewModel: OtpVerificationViewModel by viewModels()

    private var _binding: FragmentOtpVerificationBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.repeatCounter.value = false
        if ( viewModel.timer != null ) {
            viewModel.timer?.cancel()
            viewModel.timer?.onFinish()
        }
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            resendOtpButton.setOnClickListener {
                viewModel.resendOtp()
            }
            otpView.otpListener = otpListener
        }


        viewModel.apply {
            authResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is Loading -> handleLoading()
                    is Idle -> handleIdle()
                    is StateError -> handleError(response.message.toString())
                    is Success -> handleCheckCodeSuccess(response.data)
                    is AuthenticationState.SuccessResendOtp -> handleResendCodeSuccess()
                    is InputError -> {}
                    is ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    is ResendCodeSuccess -> handleResendCodeSuccess()
                    else -> {}
                }
            }
            viewState.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { data ->
                if (data.phone.isNullOrEmpty()) {
                    binding.titleTextView.text = getString(R.string.verify_your_email)
                    binding.subTitleTextView.text = getString(R.string.otp_sent_to_your_email)
                } else {
                    binding.titleTextView.text = getString(R.string.verify_your_phone_number)
                    binding.subTitleTextView.text =
                        getString(R.string.otp_sent_to_phone_number, data.phone.toString())
                }
            }

            timer = object: CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = millisUntilFinished / 1000
                    repeatCounter.value = false
                    val secondsText  = NumberHelper.persianToEnglishText(seconds.toString()).toDouble()
                    val first : Double = NumberHelper.persianToEnglishText((secondsText / 60).toString()).toDouble()
                    val second : Double = NumberHelper.persianToEnglishText((secondsText % 60).toString()).toDouble()
                    val minutes = first.toString().substring(0 , 1)+":"+second.toString().substring(0 , 2)
                    binding.otpExpireCounterTextView.text =
                        HtmlCompat.fromHtml(
                            getString(R.string.otp_expires, minutes.toString()),
                            HtmlCompat.FROM_HTML_MODE_COMPACT
                        )
                    binding.resendOtpButton.disable()
                }

                override fun onFinish() {
                    binding.otpExpireCounterTextView.text = "00:00"
                    binding.resendOtpButton.enable()
                }
            }
            timer?.start()
            repeatCounter.observe(viewLifecycleOwner) {
                if (it)
                {
                    binding.resendOtpButton.setTextColor(requireContext().getColor(R.color.secondaryColor))
                    timer = object: CountDownTimer(60000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val seconds = millisUntilFinished / 1000
                            val secondsText  = NumberHelper.persianToEnglishText(seconds.toString()).toDouble()
                            val first : Double = NumberHelper.persianToEnglishText((secondsText / 60).toString()).toDouble()
                            val second : Double = NumberHelper.persianToEnglishText((secondsText % 60).toString()).toDouble()

                            val minutes = first.toString().substring(0 , 1)+":"+second.toString().substring(0 , 2)
                            binding.otpExpireCounterTextView.text =
                                HtmlCompat.fromHtml(
                                    getString(R.string.otp_expires, minutes.toString()),
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                )
                            binding.resendOtpButton.disable()
                            repeatCounter.value = false
                        }

                        override fun onFinish() {
                            binding.otpExpireCounterTextView.text = "00:00"
                            binding.resendOtpButton.enable()
                        }
                    }
                    if (viewModel.timer != null )
                        timer?.start()
                }
                else{
                    binding.resendOtpButton.setTextColor(requireContext().getColor(R.color.mainBlack60))
                }
            }
        }
    }


    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        viewModel.clearViewState()
        binding.otpView.showError()
        binding.otpErrorMassageTextView.text = message
        MyAnimator().animateShakeX(binding.otpErrorMassageTextView)
    }

    private fun handleCheckCodeSuccess(user: User?) {
        viewModel.clearViewState()
        viewModel.saveToken(user)
        binding.otpView.showSuccess()
        viewModel.viewModelScope.launch {
            if (viewModel.timer != null)
            {
                viewModel.timer?.cancel()
                viewModel.timer?.onFinish()
            }
            delay(500)
            if ((findNavController().currentDestination?.id ?: 0) == R.id.navigationOtpVerificationFragment)
                findNavController().navigate(com.codeIn.myCash.ui.authentication.log_in.otp_verification.OtpVerificationFragmentDirections.actionNavigationOtpVerificationFragmentToNavigationResetPasswordFragment())
        }
    }

    private fun handleResendCodeSuccess() {
        viewModel.clearViewState()
        binding.otpView.setOTP("")
        viewModel.repeatCounter.value = true
    }


    /**
     * This is the listener for the OTP box. It is used to listen to the user's interaction with the OTP box.
     * It is also used to listen to the user's input and check if the input is correct or not.
     */
    private val otpListener = object : OTPListener {
        override fun onInteractionListener() {
            // fired when user types something in the Otpbox
            binding.otpErrorMassageTextView.gone()
        }

        override fun onOTPComplete(otp: String) {
            // fired when user has entered the OTP fully.
            hideKeyboard(requireContext(), view)
            viewModel.confirmOtp(otp)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.firstTime.value!!)
        {
            if ( viewModel.timer != null )
            {
                viewModel.timer?.cancel()
                viewModel.timer?.onFinish()
            }
            viewModel.firstTime.value = false
        }
    }

}