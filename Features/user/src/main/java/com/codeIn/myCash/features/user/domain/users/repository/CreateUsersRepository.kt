package com.codeIn.myCash.features.user.domain.users.repository

import com.codeIn.myCash.features.user.data.users.model.response.UsersState

interface CreateUsersRepository {
    suspend fun createUser(name: String, email: String, phone: String?,
                           password: String, status: Int, branchId : String? , type : String?): UsersState
}