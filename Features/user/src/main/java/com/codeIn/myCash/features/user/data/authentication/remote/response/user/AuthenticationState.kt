package com.codeIn.myCash.features.user.data.authentication.remote.response.user

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class AuthenticationState {
    data class Success(val data : User?) : AuthenticationState()
    data class SuccessResendOtp(val data : User?) : AuthenticationState()
    data class ServerError(val error : ErrorEntity) : AuthenticationState()
    data class StateError(val message : String?) : AuthenticationState()
    data object Idle : AuthenticationState()
    data object Loading : AuthenticationState()
    data object UnAuthorized : AuthenticationState()
    data class InputError(val inputError : InvalidInput) : AuthenticationState()
    data object ResendCodeSuccess : AuthenticationState()

}
