package com.codeIn.myCash.ui.options.profile.otp_update

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AuthConsts
import com.codeIn.common.data.AuthType
import com.codeIn.common.util.GeneralResponse
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationResponse
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.usecases.CheckCodeUseCase
import com.codeIn.myCash.features.user.domain.authentication.usecases.ResendCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OtpUpdateProfileViewModel @Inject constructor(
    private val checkCodeUseCase: CheckCodeUseCase,
    private val resendCodeUseCase: ResendCodeUseCase,
    private val state: SavedStateHandle,
) : ViewModel() {
    private val _authResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val authResult = _authResult.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()

    private val _registerType = MutableStateFlow<Int>(0)
    val registerType = _registerType.asStateFlow()

    init {
        if (state.contains(AuthConsts.PHONE_NUMBER)) {
            val phoneNumber: String = state[AuthConsts.PHONE_NUMBER] ?: ""
            _phoneNumber.value = phoneNumber
        }

        if (state.contains(AuthConsts.REGISTER_TYPE))
            _registerType.value = state[AuthConsts.REGISTER_TYPE]!!
    }

//    fun checkCode(otp: String?) {
//        launchIO {
//            _authResult.emit(AuthenticationResponse.Loading)
//            checkCodeUseCase.invoke(
//                otp, phoneNumber.value, "1", AuthType.PHONE.value.toString(), null, "1"
//            ).let {
//                _authResult.emit(it)
//            }
//        }
//    }

    fun resendOtp(phone: String? = null, email: String? = null) {
        launchIO {
            _authResult.emit(AuthenticationState.Loading)
            resendCodeUseCase.invoke(phone ?: phoneNumber.value, "1" , type = AuthType.PHONE.value.toString() , email = email).let {
                _authResult.emit(it)
            }
        }
    }

    fun clearViewState() {
        if (_authResult.value !is AuthenticationState.Idle)
            _authResult.value = AuthenticationState.Idle
    }

}