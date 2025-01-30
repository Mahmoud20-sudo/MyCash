package com.codeIn.myCash.integration.model.network.authentication.signUp

import com.codeIn.myCash.integration.model.dataClasses.GeneralResponse
import com.codeIn.myCash.integration.model.dataClasses.authentication.user.UserResponse
import com.codeIn.myCash.integration.model.network.authentication.AuthenticationEndPoints
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface OtpApiServices {

    /**
     * requestBody: { "phone", "code", "country_id", "type(1: phone, 2: email)" }
     */
    @POST(AuthenticationEndPoints.CHECK_CODE)
    suspend fun checkCode(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<UserResponse>

    /**
     * requestBody: { "phone/email", "country_id" }
     */
    @POST(AuthenticationEndPoints.RESEND_CODE)
    suspend fun resendCode(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<GeneralResponse>

}