package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState

interface ResendCodeRepository  {
    suspend fun resendCode(phone : String? , countryId : String? ,email : String? , type : String?) : AuthenticationState
}