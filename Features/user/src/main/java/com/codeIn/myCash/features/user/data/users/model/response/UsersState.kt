package com.codeIn.myCash.features.user.data.users.model.response

import com.codeIn.common.domain.ErrorEntity

sealed class UsersState {
    data object Idle : UsersState()
    data object Loading : UsersState()
    data class SuccessRetrieveUsers(val data: GetUsersDTO?) : UsersState()
    data class SuccessDeleteAllUsers(val data: DeleteDTO?) : UsersState()
    data class SuccessDeleteSingleUser(val data: DeleteDTO?) : UsersState()

    data class SuccessCreateUser(val data: CreateUserDTO?) : UsersState()

    data class ServerError(val error: ErrorEntity) : UsersState()
    data class StateError(val message: String?) : UsersState()
    data object UnAuthorized : UsersState()
}