package com.codeIn.myCash.ui.authentication.log_in.otp_verification_account

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AuthConsts
import com.codeIn.common.data.AuthType
import com.codeIn.common.util.launchIO
import com.codeIn.common.data.AuthConsts.PHONE_NUMBER
import com.codeIn.common.data.Code
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.usecases.CheckCodeUseCase
import com.codeIn.myCash.features.user.domain.authentication.usecases.ResendCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OtpVerificationSignUpViewModel @Inject constructor(
    private val checkCodeUseCase: CheckCodeUseCase,
    private val resendCodeUseCase: ResendCodeUseCase,
    private val state: SavedStateHandle,
    val prefs : SharedPrefsModule,
) : ViewModel() {
    private val _authResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val authResult = _authResult.asStateFlow()


    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()

    var timer : CountDownTimer? = null
    var firstTime  = MutableLiveData<Boolean>(true)
    var repeatCounter = MutableLiveData<Boolean>(false)

    init {
        if (state.contains(PHONE_NUMBER)) {
            val phoneNumber: String = state[PHONE_NUMBER] ?: ""
            _phoneNumber.value = phoneNumber
        }

    }

    fun checkCode(otp: String?) {
        launchIO {
            _authResult.emit(AuthenticationState.Loading)

            checkCodeUseCase.invoke(
                otp, phoneNumber.value, "1", AuthType.PHONE.value.toString(), null, "1" ,
                key = Code.REGISTER.value.toString()
            ).let {
                _authResult.emit(it)
            }
        }
    }

    fun resendOtp() {
        launchIO {
            resendCodeUseCase.invoke(phoneNumber.value, "1" , type = AuthType.PHONE.value.toString() , email = null).let {
                _authResult.emit(it)
            }
        }
    }

    fun clearViewState() {
        if (_authResult.value !is AuthenticationState.Idle)
            _authResult.value = AuthenticationState.Idle
    }

}