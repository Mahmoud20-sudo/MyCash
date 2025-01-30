package com.codeIn.myCash.features.user.domain.users.repository

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import retrofit2.Response

interface UsersRepository {
    suspend fun getUser(searchText : String?): UsersState
}