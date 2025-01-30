package com.codeIn.myCash.features.user.domain.users.repository

import com.codeIn.myCash.features.user.data.users.model.response.UsersState

interface UpdateUsersRepository {
    suspend fun updateUsers(
        name: String,
        email: String,
        phone: String?,
        password: String,
        note:String,
        employeeId:Int ,
        branchId : String? ,
        type : String?
    ): UsersState
}