package com.codeIn.myCash.features.user.domain.users.repository

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import retrofit2.Response

interface DeleteAllEmployeeRepository {
    suspend fun deleteAllEmployee(deleteAll :Int): UsersState
}