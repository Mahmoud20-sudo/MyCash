package com.codeIn.myCash.features.user.data.users.model.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class UsersResponse {
    data class Success(val data : GetUsersDTO?) : UsersResponse()
    data class ServerError(val error : ErrorEntity) : UsersResponse()
    data class ResponseError(val message : String?) : UsersResponse()
    data object Idle : UsersResponse()
    data object Loading : UsersResponse()
    data class InputError(val inputError : InvalidInput) : UsersResponse()
}