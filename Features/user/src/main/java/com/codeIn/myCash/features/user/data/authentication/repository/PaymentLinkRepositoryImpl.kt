package com.codeIn.myCash.features.user.data.authentication.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.domain.authentication.repository.PaymentLinkRepository
import javax.inject.Inject

class PaymentLinkRepositoryImpl @Inject constructor (private var authentication: Authentication,
                                                     private val prefs : SharedPrefsModule,
                                                     private val errorHandler: ErrorHandlerImpl) : PaymentLinkRepository {
    override suspend fun getPaymentLink(
        influenceId: String?,
        packageId: String?,
        deviceId: String?
    ): GeneralResponse {
        return try{

            val lang  = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            authentication.getPaymentLink(lang , token , influenceId , deviceId , packageId).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        GeneralResponse.Success(response.body()?.data.toString())
                    }
                    else {
                        GeneralResponse.ResponseError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    GeneralResponse.UnAuthorized
                }
                else{
                    GeneralResponse.ServerError(errorHandler.invoke(response.code()))
                }
            }

        }catch (throwable : Throwable){
            GeneralResponse.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getRenewPaymentLink(
        influenceId: String?,
        packageId: String?,
        deviceId: String?
    ): GeneralResponse {
        return try{

            val lang  = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            authentication.getRenewPaymentLink(lang , token , influenceId , deviceId , packageId).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        GeneralResponse.Success(response.body()?.data.toString())
                    }
                    else {
                        GeneralResponse.ResponseError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    GeneralResponse.UnAuthorized
                }
                else{
                    GeneralResponse.ServerError(errorHandler.invoke(response.code()))
                }
            }

        }catch (throwable : Throwable){
            GeneralResponse.ServerError(errorHandler.getError(throwable))
        }
    }
}