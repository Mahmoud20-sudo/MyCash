package com.codeIn.myCash.integration.model.network.authentication.signUp


import com.codeIn.myCash.integration.model.dataClasses.authentication.devices.DevicesResponse
import com.codeIn.myCash.integration.model.dataClasses.authentication.packages.PackagesResponse
import com.codeIn.myCash.integration.model.network.authentication.AuthenticationEndPoints
import retrofit2.Response
import retrofit2.http.*

interface AuthGeneralDataApiServices {
    @GET(AuthenticationEndPoints.GET_PACKAGES)
    suspend fun getPackages(@HeaderMap headers: Map<String, String>): Response<PackagesResponse>

    @GET(AuthenticationEndPoints.GET_DEVICES)
    suspend fun getDevices(@HeaderMap headers: Map<String, String>): Response<DevicesResponse>

}