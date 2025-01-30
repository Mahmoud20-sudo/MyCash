package com.codeIn.myCash.integration.model.network.authentication.login

import com.codeIn.myCash.integration.model.dataClasses.authentication.user.UserResponse
import com.codeIn.myCash.integration.model.network.authentication.AuthenticationEndPoints
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST


interface SignInApiServices {

    /**
     * requestBody: { "phone", "password", "country_id" }
     */
    @POST(AuthenticationEndPoints.LOGIN)
    suspend fun logIn(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<UserResponse>

}