package com.codeIn.help.data.model.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class HelpResponse {
    data class Success(val data : GetHelpDTO?) : HelpResponse()
    data class ServerError(val error : ErrorEntity) : HelpResponse()
    data class ResponseError(val message : String?) : HelpResponse()
    data object Idle : HelpResponse()
    data object Loading : HelpResponse()
    data class InputError(val inputError : InvalidInput) : HelpResponse()
}