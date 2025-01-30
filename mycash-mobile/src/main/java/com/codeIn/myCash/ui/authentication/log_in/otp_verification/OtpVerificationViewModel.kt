package com.codeIn.myCash.ui.authentication.log_in.otp_verification

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AuthType
import com.codeIn.common.data.Code
import com.codeIn.common.util.GeneralResponse
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import com.codeIn.myCash.features.user.domain.authentication.usecases.CheckCodeUseCase
import com.codeIn.myCash.features.user.domain.authentication.usecases.ResendCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OtpVerificationViewModel @Inject constructor(
    private val useCase: CheckCodeUseCase,
    private val state: SavedStateHandle,
    private val resendCodeUseCase: ResendCodeUseCase,
    ) : ViewModel() {

    private val _authResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val authResult = _authResult.asStateFlow()

    private val _viewState = MutableStateFlow(OtpVerificationState())
    val viewState = _viewState.asStateFlow()

    var timer : CountDownTimer? = null
    var firstTime  = MutableLiveData<Boolean>(true)
    var repeatCounter = MutableLiveData<Boolean>(false)
    init {
        if (state.contains("phoneNumber")) {
            _viewState.value.phone = state["phoneNumber"] ?: ""
            if (_viewState.value.phone?.isNotEmpty() == true)
            {
                _viewState.value.countryId = "1"
                _viewState.value.type = AuthType.PHONE.value.toString()
                _viewState.value.email = null
            }
        }
        if (state.contains("email")) {
            _viewState.value.email = state["email"] ?: ""
            if (_viewState.value.email?.isNotEmpty() == true){
                _viewState.value.type = AuthType.EMAIL.value.toString()
                _viewState.value.phone = null
                _viewState.value.countryId = null
            }

        }
    }


    fun confirmOtp(otp: String) {
        launchIO {
            _authResult.emit(AuthenticationState.Loading)
            useCase.invoke(
                otp = otp, phone = _viewState.value.phone, countryId = "1",
                email = _viewState.value.email, type = _viewState.value.type, active = null , key = Code.FORGET.value.toString()
            ).let {
                _authResult.emit(it)
                Log.d("TAG" , "data of check code $it")
            }
        }
    }

    fun resendOtp() {
        launchIO {
            resendCodeUseCase.invoke(phone = _viewState.value.phone, countryId = "1" , type = _viewState.value.type , email = _viewState.value.email).let {
                _authResult.emit(it)
            }
        }
    }

    fun clearViewState() {
        if (_authResult.value !is AuthenticationState.Idle)
            _authResult.value = AuthenticationState.Idle
    }

    fun saveToken(user: User?) {
        state["tokenReset"] = user?.token
    }

}