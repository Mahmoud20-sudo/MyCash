package com.codeIn.myCash.features.user.data.authentication.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.ResetPasswordRepository
import com.codeIn.myCash.features.user.domain.authentication.usecases.ResetPasswordValidationUseCase
import javax.inject.Inject

class ResetPasswordRepositoryImpl @Inject constructor (private var authentication: Authentication,
                                                       private val prefs : SharedPrefsModule,
                                                       private val errorHandler: ErrorHandlerImpl,
                                                       private val resetPasswordValidationUseCase: ResetPasswordValidationUseCase
) : ResetPasswordRepository {

    override suspend fun resetPassword(
        password: String?,
        confirmPassword: String?,
    ): AuthenticationState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.forgetToken())

            resetPasswordValidationUseCase.invoke(password , confirmPassword ).let {
                if (it != InvalidInput.NONE) return AuthenticationState.InputError(it)
            }
            authentication.resetPassword(
                lang = lang,
                token,
                password
            ).let { response ->
                Log.d("TAG" , "reset password $response")
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        val user = response.body()?.data
                        prefs.putValue(Constants.getToken() , "Bearer "+user?.token)
                        prefs.putValue(Constants.getCurrency() , user?.country?.currency)
                        prefs.putValue(Constants.logoStore() , user?.accountInfo?.logo)
                        prefs.putValue(Constants.nameStore() , user?.accountInfo?.commercialRecordName)
                        AuthenticationState.Success(user)
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
