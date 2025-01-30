package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState

interface ForgetPasswordRepository {

    suspend fun sendOtp(key : String , type : String , countryId : String?) : AuthenticationState
}