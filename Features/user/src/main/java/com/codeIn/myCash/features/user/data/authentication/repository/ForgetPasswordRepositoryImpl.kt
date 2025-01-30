package com.codeIn.myCash.features.user.data.authentication.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.ForgetPasswordRepository
import com.codeIn.myCash.features.user.domain.authentication.usecases.SendOtpValidationUseCase
import javax.inject.Inject

class ForgetPasswordRepositoryImpl @Inject constructor (private var authentication: Authentication,
                                                        private val prefs : SharedPrefsModule,
                                                        private val errorHandler: ErrorHandlerImpl,
                                                        private val sendOtpValidationUseCase: SendOtpValidationUseCase
) : ForgetPasswordRepository {
    override suspend fun sendOtp(key: String, type: String, countryId: String?) : AuthenticationState {
        return try {

            val lang = prefs.getValue(Constants.getLang())

            sendOtpValidationUseCase.invoke(key , type).let {
                if (it != InvalidInput.NONE) return AuthenticationState.InputError(it)
            }
            authentication.forgetPassword(
                lang = lang,
                key = key,
                countryId = countryId,
                type = type,
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        Log.d("TAG" , "code is ${response.body()?.data?.msgCode}")
                        AuthenticationState.Success(response.body()?.data)
                    } else {
                        AuthenticationState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    AuthenticationState.UnAuthorized
                } else {
                    AuthenticationState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        } catch (throwable: Throwable) {
            AuthenticationState.ServerError(errorHandler.getError(throwable))
        }
    }
}
