package com.codeIn.myCash.features.user.domain.users.repository

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import retrofit2.Response

interface DeleteUserRepository {
    suspend fun deleteUser(employeeId: Int): UsersState
}