package com.codeIn.myCash.integration.model.remoteSource.authentication.signUp

import com.codeIn.myCash.integration.model.dataClasses.ErrorConverter
import com.codeIn.myCash.integration.model.dataClasses.authentication.devices.DevicesResponse
import com.codeIn.myCash.integration.model.dataClasses.authentication.packages.PackagesResponse
import com.codeIn.myCash.integration.model.network.NetworkParams
import com.codeIn.myCash.integration.model.network.authentication.signUp.AuthGeneralDataApiServices
import javax.inject.Inject

class AuthGeneralDataRemoteSource @Inject constructor(
    private val apiServices: AuthGeneralDataApiServices,
    private val networkParams: NetworkParams
) {

    suspend fun getPackages(headers: Map<String, String> = networkParams.headers): PackagesResponse {
        val response = apiServices.getPackages(headers)
        return if (response.isSuccessful)
            response.body() ?: PackagesResponse(status = 0, message = "An error occurred")
        else
            ErrorConverter.convert(response.errorBody()?.string())

    }

    suspend fun getDevices(headers: Map<String, String> = networkParams.headers): DevicesResponse {
        val response = apiServices.getDevices(headers)
        return if (response.isSuccessful)
            response.body() ?: DevicesResponse(status = 0, message = "An error occurred")
        else
            ErrorConverter.convert(response.errorBody()?.string())
    }
}