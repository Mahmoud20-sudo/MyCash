package com.codeIn.myCash.features.user.data.complete_data.model

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO

sealed class CompleteInfoState {
    data object Idle : CompleteInfoState()
    data object Loading : CompleteInfoState()
    data class SuccessCompleteData(val data: UserDTO?) : CompleteInfoState()
    data class SuccessLogout(val data: BaseResponseDTO?) : CompleteInfoState()
    data class InputError(val inputError : InvalidInput) : CompleteInfoState()
    data class ServerError(val error: ErrorEntity) : CompleteInfoState()
    data class StateError(val message: String?) : CompleteInfoState()
    data object UnAuthorized : CompleteInfoState()
}