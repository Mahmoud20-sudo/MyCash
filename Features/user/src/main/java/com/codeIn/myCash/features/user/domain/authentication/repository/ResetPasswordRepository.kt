package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState

interface ResetPasswordRepository {

    suspend fun resetPassword(password : String? , confirmPassword :String?) : AuthenticationState
}