package com.codeIn.myCash.integration.model.remoteSource.authentication.signUp

import com.codeIn.myCash.integration.model.dataClasses.ErrorConverter
import com.codeIn.myCash.integration.model.dataClasses.authentication.user.UserResponse
import com.codeIn.myCash.integration.model.network.NetworkParams
import com.codeIn.myCash.integration.model.network.RequestsKeys.COUNTRY_ID
import com.codeIn.myCash.integration.model.network.RequestsKeys.DEVICE_COUNTRY_ID
import com.codeIn.myCash.integration.model.network.RequestsKeys.EMAIL
import com.codeIn.myCash.integration.model.network.RequestsKeys.INFLUENCER_ID
import com.codeIn.myCash.integration.model.network.RequestsKeys.PACKAGE_ID
import com.codeIn.myCash.integration.model.network.RequestsKeys.PASSWORD
import com.codeIn.myCash.integration.model.network.RequestsKeys.PHONE
import com.codeIn.myCash.integration.model.network.authentication.signUp.RegisterApiServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class RegisterRemoteSource @Inject constructor(
    private val apiServices: RegisterApiServices,
    private val networkParams: NetworkParams
) {

    suspend fun register(
        packageId: Int,
        deviceId: Int,
        countryId: Int,
        phoneNumber: String,
        password: String,
        email: String,
        influencerId: String
    ): UserResponse {
        val res = apiServices.register(
            headers = networkParams.headers,
            requestBody = getRegistrationRequestBody(
                packageId = packageId,
                deviceId = deviceId,
                countryId = countryId,
                phoneNumber = phoneNumber,
                password = password,
                email = email,
                influencerId = influencerId
            )
        )

        return if (res.isSuccessful)
            res.body() ?: UserResponse(status = 0, message = "An error occurred")
        else
            ErrorConverter.convert(res.errorBody()?.string())
    }

    private fun getRegistrationRequestBody(
        packageId: Int,
        deviceId: Int,
        countryId: Int,
        phoneNumber: String,
        password: String,
        email: String,
        influencerId: String
    ): RequestBody {

        val jsonReq = JSONObject()
        jsonReq.put(PACKAGE_ID, packageId)
        jsonReq.put(DEVICE_COUNTRY_ID, deviceId)
        jsonReq.put(EMAIL, email)
        jsonReq.put(PHONE, phoneNumber)
        jsonReq.put(COUNTRY_ID, countryId)
        jsonReq.put(PASSWORD, password)
        jsonReq.put(INFLUENCER_ID, influencerId)

        return jsonReq.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

}