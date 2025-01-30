package com.codeIn.myCash.integration.model.remoteSource.authentication.login

import com.codeIn.myCash.integration.model.dataClasses.ErrorConverter
import com.codeIn.myCash.integration.model.dataClasses.authentication.AuthType
import com.codeIn.myCash.integration.model.dataClasses.authentication.user.UserResponse
import com.codeIn.myCash.integration.model.network.NetworkParams
import com.codeIn.myCash.integration.model.network.RequestsKeys.COUNTRY_ID
import com.codeIn.myCash.integration.model.network.RequestsKeys.PASSWORD
import com.codeIn.myCash.integration.model.network.RequestsKeys.PHONE
import com.codeIn.myCash.integration.model.network.RequestsKeys.TYPE
import com.codeIn.myCash.integration.model.network.authentication.login.SignInApiServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class LogInRemoteSource @Inject constructor(
    private val apiServices: SignInApiServices,
    private val networkParams: NetworkParams
) {

    suspend fun logIn(
        phone: String,
        password: String,
        countryId: Int,
    ): UserResponse {

        val response = apiServices.logIn(
            headers = networkParams.headers,
            requestBody = getLogInRequestBody(
                phone = phone,
                password = password,
                countryId = countryId
            )
        )

        return if (response.isSuccessful)
            response.body()?.let {
                it.data?.token?.let { token ->
                    networkParams.setAuthToken(token)
                }
                it
            } ?: UserResponse(status = 0, message = "An error occurred")
        else
            ErrorConverter.convert(response.errorBody()?.string())
    }


    private fun getLogInRequestBody(phone: String, password: String, countryId: Int): RequestBody {
        val jsonReq = JSONObject()
        jsonReq.put(PHONE, phone)
        jsonReq.put(PASSWORD, password)
        jsonReq.put(COUNTRY_ID, countryId)
        jsonReq.put(TYPE, AuthType.PHONE.name)
        return jsonReq.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}