package com.codeIn.myCash.integration.model.network.authentication.signUp

import com.codeIn.myCash.integration.model.dataClasses.authentication.user.UserResponse
import com.codeIn.myCash.integration.model.network.authentication.AuthenticationEndPoints
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface RegisterApiServices {

    /**
     * requestBody contains:
     * { "phone", "password", "country_code", "device_id", "package_id", "email", "influencer_code" }
     */
    @POST(AuthenticationEndPoints.REGISTER)
    suspend fun register(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<UserResponse>

}