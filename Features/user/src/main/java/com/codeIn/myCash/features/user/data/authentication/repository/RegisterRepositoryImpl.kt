package com.codeIn.myCash.features.user.data.authentication.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.RegisterType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.Validation
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.RegisterRepository
import com.codeIn.myCash.features.user.domain.authentication.usecases.RegisterValidationUseCase
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor (private var authentication: Authentication,
                                                  private val prefs : SharedPrefsModule,
                                                  private val errorHandler: ErrorHandlerImpl,
                                                  private val validation: Validation
): RegisterRepository{
    override suspend fun register(
        phone: String,
        countryId: String,
        email: String,
        password: String,
        type: Int
    ): AuthenticationState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())

            validation.validate(
                email = email,
                phoneNumberSaudi = phone,
                password = password,
                )
                .let { if (it != InvalidInput.NONE) return AuthenticationState.InputError(it) }

            val typeParams = if (type == RegisterType.FREE.value)
                "1"
            else "26"

            authentication.register(
                lang= lang ,
                phone= phone ,
                password= password,
                countryId = countryId ,
                email = email,
                packageId = typeParams
                ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        val user = response.body()?.data
                        Log.d("TAG" , "code is ${response.body()?.data?.msgCode}")
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