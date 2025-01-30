package com.codeIn.myCash.integration.model.network.authentication.login

import com.codeIn.myCash.integration.model.dataClasses.GeneralResponse
import com.codeIn.myCash.integration.model.network.authentication.AuthenticationEndPoints
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ForgotPasswordApiServices {

    /**
     * requestBody: { "phone/email", "country_id", "type(1: phone, 2: email)" }
     */
    @POST(AuthenticationEndPoints.FORGET_PASSWORD)
    suspend fun forgotPassword(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<GeneralResponse>


    /**
     * requestBody: { "password" }
     */
    @POST(AuthenticationEndPoints.RESET_PASSWORD)
    suspend fun resetPassword(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<GeneralResponse>


}