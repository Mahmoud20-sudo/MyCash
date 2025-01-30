package com.codeIn.myCash.features.user.data.authentication.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.ResendCodeRepository
import javax.inject.Inject

class ResendCodeRepositoryImpl @Inject constructor (private var authentication: Authentication,
                                                    private val prefs : SharedPrefsModule,
                                                    private val errorHandler: ErrorHandlerImpl) : ResendCodeRepository {
    override suspend fun resendCode(
        phone: String?,
        countryId: String?,
        email : String? ,
        type : String?
    ) : AuthenticationState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            authentication.resendCode(
                lang= lang ,
                phone = phone,
                countryId = countryId,
                email= email,
                type= type
            ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        AuthenticationState.SuccessResendOtp(response.body()?.data)
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