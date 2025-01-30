package com.codeIn.myCash.ui.authentication.log_in.otp_verification_account
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.offline.Constants
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.disable
import com.codeIn.common.util.enable
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentOtpVerificationSignUpBinding
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.utilities.MyAnimator
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import dagger.hilt.android.AndroidEntryPoint
import `in`.aabhasjindal.otptextview.OTPListener
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtpVerificationSignUpFragment : Fragment() {

    private val viewModel: OtpVerificationSignUpViewModel by viewModels()

    private var _binding: FragmentOtpVerificationSignUpBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpVerificationSignUpBinding.inflate(inflater, container, false)
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

            binding.otpView.otpListener = otpListener
        }

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
                    is AuthenticationState.Success -> handleCheckCodeSuccess(response.data)
                    is AuthenticationState.SuccessResendOtp -> handleResendCodeSuccess()
                    is AuthenticationState.InputError -> {}
                    is AuthenticationState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    is AuthenticationState.ResendCodeSuccess -> handleResendCodeSuccess()
                    else -> {}
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

    private fun handleCheckCodeSuccess(user : User?) {
        viewModel.clearViewState()
        binding.otpView.showSuccess()
        viewModel.viewModelScope.launch {
            if (viewModel.timer != null)
            {
                viewModel.timer?.cancel()
                viewModel.timer?.onFinish()
            }
            viewModel.prefs.putValue(Constants.getToken() , "Bearer ${user?.token.toString()}")
            viewModel.prefs.putValue(Constants.getCurrency() , user?.country?.currency)
            viewModel.prefs.putValue(Constants.logoStore() , user?.accountInfo?.logo)
            viewModel.prefs.putValue(Constants.nameStore() , user?.accountInfo?.commercialRecordName)
            val intent = Intent(requireActivity() , MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun handleResendCodeSuccess() {
        viewModel.clearViewState()
        binding.otpView.setOTP("")
        viewModel.repeatCounter.value = true
    }

    private val otpListener = object : OTPListener {
        override fun onInteractionListener() {
            // fired when user types something in the Otpbox
            binding.otpErrorMassageTextView.gone()
        }

        override fun onOTPComplete(otp: String) {
            // fired when user has entered the OTP fully.
            hideKeyboard(requireContext(), view)
            viewModel.checkCode(otp)
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