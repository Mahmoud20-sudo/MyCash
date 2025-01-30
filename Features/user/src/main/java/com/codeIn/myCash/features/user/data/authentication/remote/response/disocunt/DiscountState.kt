package com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class DiscountState{
    data class Success(val data : DiscountCodeData?) : DiscountState()
    data class ServerError(val error : ErrorEntity) : DiscountState()
    data class StateError(val message : String?) : DiscountState()
    data object Idle : DiscountState()
    data object Loading : DiscountState()
    data object UnAuthorized : DiscountState()
    data class InputError(val inputError : InvalidInput) : DiscountState()
}