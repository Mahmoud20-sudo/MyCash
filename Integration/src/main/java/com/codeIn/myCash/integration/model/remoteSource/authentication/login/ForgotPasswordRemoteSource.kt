package com.codeIn.myCash.integration.model.remoteSource.authentication.login

import com.codeIn.myCash.integration.model.dataClasses.ErrorConverter
import com.codeIn.myCash.integration.model.dataClasses.GeneralResponse
import com.codeIn.myCash.integration.model.dataClasses.authentication.AuthType
import com.codeIn.myCash.integration.model.network.NetworkParams
import com.codeIn.myCash.integration.model.network.RequestsKeys.COUNTRY_ID
import com.codeIn.myCash.integration.model.network.RequestsKeys.KEY
import com.codeIn.myCash.integration.model.network.RequestsKeys.PASSWORD
import com.codeIn.myCash.integration.model.network.RequestsKeys.TYPE
import com.codeIn.myCash.integration.model.network.authentication.login.ForgotPasswordApiServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class ForgotPasswordRemoteSource @Inject constructor(
    private val apiServices: ForgotPasswordApiServices,
    private val networkParams: NetworkParams
) {

    suspend fun forgotPassword(
        phone: String? = null,
        email: String? = null,
        countryId: Int,
    ): GeneralResponse {

        val response = apiServices.forgotPassword(
            headers = networkParams.headers,
            requestBody = getForgotPasswordRequestBody(
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

    suspend fun resetPassword(password: String): GeneralResponse {

        val response = apiServices.resetPassword(
            headers = networkParams.headers,
            requestBody = getResetPasswordRequestBody(password)
        )

        return if (response.isSuccessful)
            response.body() ?: GeneralResponse(status = 0, message = "An error occurred")
        else
            ErrorConverter.convert(response.errorBody()?.string())
    }


    private fun getResetPasswordRequestBody(password: String): RequestBody {
        val jsonReq = JSONObject()
        jsonReq.put(PASSWORD, password)
        return jsonReq.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

    private fun getForgotPasswordRequestBody(
        phone: String? = null,
        email: String? = null,
        countryId: Int
    ): RequestBody {

        val jsonReq = JSONObject()
        phone?.let {
            jsonReq.put(KEY, it)
            jsonReq.put(TYPE, AuthType.PHONE.value)
        }
        email?.let {
            jsonReq.put(KEY, it)
            jsonReq.put(TYPE, AuthType.EMAIL.value)
        }
        jsonReq.put(COUNTRY_ID, countryId)

        return jsonReq.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

}