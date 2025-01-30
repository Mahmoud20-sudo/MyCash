package com.codeIn.myCash.features.user.data.authentication.remote.response.user

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class AuthenticationResponse {
    data class Success(val data : User?) : AuthenticationResponse()
    data class ServerError(val error : ErrorEntity) : AuthenticationResponse()
    data class ResponseError(val message : String?) : AuthenticationResponse()
    data object Idle : AuthenticationResponse()
    data object Loading : AuthenticationResponse()
    data class InputError(val inputError : InvalidInput) : AuthenticationResponse()
    data object ResendCodeSuccess : AuthenticationResponse()

}
