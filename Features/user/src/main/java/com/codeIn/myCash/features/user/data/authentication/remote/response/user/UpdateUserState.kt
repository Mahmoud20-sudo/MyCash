package com.codeIn.myCash.features.user.data.authentication.remote.response.user

import com.codeIn.common.domain.ErrorEntity

sealed class UpdateUserState{
    data class Success(val user : AccountInfo?) : UpdateUserState()
    data class ServerError(val error : ErrorEntity) : UpdateUserState()
    data class StateError(val message : String?) : UpdateUserState()
    data object Idle : UpdateUserState()
    data object Loading : UpdateUserState()
}
