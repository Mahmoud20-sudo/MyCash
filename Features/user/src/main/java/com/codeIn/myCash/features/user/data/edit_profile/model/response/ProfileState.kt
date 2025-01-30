package com.codeIn.myCash.features.user.data.edit_profile.model.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState

sealed class ProfileState<out T> {
    data object Idle : ProfileState<Nothing>()
    data object Loading : ProfileState<Nothing>()
    data class Success<out T>(val result : T?) : ProfileState<T>()
    data class ServerError(val error : ErrorEntity) : ProfileState<Nothing>()
    data class StateError(val message : String?) : ProfileState<Nothing>()
    data class InputError(val inputError : InvalidInput) : ProfileState<Nothing>()
}