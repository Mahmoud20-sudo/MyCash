package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState

interface CheckCodeRepository {

    suspend fun checkCode(
        otp : String?,
        phone : String? ,
        countryId : String? ,
        type : String , //1=>phone , 2 =>email
        email : String? ,
        active : String?, //send 1 in check active code
        key : String?
    ) : AuthenticationState
}