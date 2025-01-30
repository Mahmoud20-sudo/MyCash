package com.codeIn.common.util

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class GeneralResponse{
    data object Idle : GeneralResponse()

    data object Loading : GeneralResponse()

    data class Success(val message : String?) : GeneralResponse()

    data class ServerError(val error : ErrorEntity) : GeneralResponse()
    data object UnAuthorized : GeneralResponse()

    data class ResponseError(val message : String?) : GeneralResponse()
    data class InputError(val inputError : InvalidInput) : GeneralResponse()
}
