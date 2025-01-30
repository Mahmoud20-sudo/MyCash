package com.codeIn.myCash.integration.model.remoteSource.authentication.signUp

import com.codeIn.myCash.integration.model.dataClasses.ErrorConverter
import com.codeIn.myCash.integration.model.dataClasses.GeneralResponse
import com.codeIn.myCash.integration.model.dataClasses.authentication.AuthType
import com.codeIn.myCash.integration.model.network.NetworkParams
import com.codeIn.myCash.integration.model.network.RequestsKeys.CODE
import com.codeIn.myCash.integration.model.network.RequestsKeys.COUNTRY_ID
import com.codeIn.myCash.integration.model.network.RequestsKeys.EMAIL
import com.codeIn.myCash.integration.model.network.RequestsKeys.PHONE
import com.codeIn.myCash.integration.model.network.RequestsKeys.TYPE
import com.codeIn.myCash.integration.model.network.authentication.signUp.OtpApiServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class OtpRemoteSource @Inject constructor(
    private val apiServices: OtpApiServices,
    private val networkParams: NetworkParams
) {

    suspend fun checkCode(
        phone: String?,
        email: String?,
        code: String,
        countryId: Int
    ): GeneralResponse {

        val response = apiServices.checkCode(
            headers = networkParams.headers,
            requestBody = getCheckCodeRequestBody(
                phone = phone,
                email = email,
                code = code,
                countryId = countryId
            )
        )

        return if (response.isSuccessful) {
            response.body()?.let {
                it.data?.token?.let { token ->
                    networkParams.setAuthToken(token)
                }
                GeneralResponse(status = it.status, message = it.message)
            } ?: GeneralResponse(status = 0, message = "An error occurred")
        } else {
            ErrorConverter.convert(response.errorBody()?.string())
        }
    }

    suspend fun resendCode(
        phone: String? = null,
        email: String? = null,
        countryId: Int
    ): GeneralResponse {

        val response = apiServices.resendCode(
            headers = networkParams.headers,
            requestBody = getResendCodeRequestBody(
                phone = phone,
                email = email,
                countryId = countryId
            )
        )

        return if (response.isSuccessful)
            response.body() ?: GeneralResponse(status = 0, message = "An error occurred")
        else
            ErrorConverter.convert(response.errorBody()?.string())
    }


    private fun getCheckCodeRequestBody(
        phone: String?,
        email: String?,
        code: String,
        countryId: Int
    ): RequestBody {
        val jsonReq = JSONObject()
        if (!phone.isNullOrBlank()) {
            jsonReq.put(PHONE, phone)
            jsonReq.put(TYPE, AuthType.PHONE.value)
        }
        if (!email.isNullOrBlank()) {
            jsonReq.put(EMAIL, email)
            jsonReq.put(TYPE, AuthType.EMAIL.value)
        }
        jsonReq.put(CODE, code)
        jsonReq.put(COUNTRY_ID, countryId)

        return jsonReq.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

    private fun getResendCodeRequestBody(
        phone: String? = null,
        email: String? = null,
        countryId: Int
    ): RequestBody {
        val jsonReq = JSONObject()
        phone?.let { jsonReq.put(PHONE, it) }
        email?.let { jsonReq.put(EMAIL, it) }
        jsonReq.put(COUNTRY_ID, countryId)
        return jsonReq.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

}