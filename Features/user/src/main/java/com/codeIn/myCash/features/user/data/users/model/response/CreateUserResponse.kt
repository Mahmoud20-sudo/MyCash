package com.codeIn.myCash.features.user.data.users.model.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class CreateUserResponse {
    data class Success(val data : CreateUserDTO?) : CreateUserResponse()
    data class ServerError(val error : ErrorEntity) : CreateUserResponse()
    data class ResponseError(val message : String?) : CreateUserResponse()
    data object Idle : CreateUserResponse()
    data object Loading : CreateUserResponse()
    data class InputError(val inputError : InvalidInput) : CreateUserResponse()
}