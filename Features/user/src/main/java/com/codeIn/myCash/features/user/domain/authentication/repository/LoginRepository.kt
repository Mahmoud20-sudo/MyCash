package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
interface LoginRepository {

    suspend fun login( phone : String , countryId : String , password : String) : AuthenticationState

}