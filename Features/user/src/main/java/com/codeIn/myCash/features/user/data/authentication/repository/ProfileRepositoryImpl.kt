package com.codeIn.myCash.features.user.data.authentication.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor (private val authentication: Authentication,
                                                 private val prefs : SharedPrefsModule,
                                                 private val errorHandler: ErrorHandlerImpl, ): ProfileRepository {
    override suspend fun getInfo(): AuthenticationState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())
            authentication.profile(
                lang = lang ,
                authorization= token
            ).let { response ->
                Log.d("TAGGG" , " data is in profile $response")
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        val user = response.body()?.data
                        AuthenticationState.Success(user)
                    }
                    else {
                        AuthenticationState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    AuthenticationState.UnAuthorized
                }
                else{
                    AuthenticationState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        }catch (throwable : Throwable){
            AuthenticationState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun enableFeaturesOfSettings(
        drafts: String?,
        notifications: String?,
        quickInvoiceMode: String?,
    ): AuthenticationState {
        return try{

            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

            authentication.enableFeaturesOfSettings(
                lang = lang ,
                authorization= token ,
                drafts= drafts ,
                notification = notifications ,
                quickInvoice = quickInvoiceMode
            ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        val user = response.body()?.data
                        AuthenticationState.Success(user)
                    }
                    else {
                        AuthenticationState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    AuthenticationState.UnAuthorized
                }
                else{
                    AuthenticationState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        }catch (throwable : Throwable){
            AuthenticationState.ServerError(errorHandler.getError(throwable))
        }
    }


}