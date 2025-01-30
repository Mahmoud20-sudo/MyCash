package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState

interface RegisterRepository {
    suspend fun register(phone : String, countryId : String , email : String,  password: String , type : Int) : AuthenticationState
}