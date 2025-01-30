package com.codeIn.myCash.features.user.data.authentication.repository

import com.codeIn.common.data.AuthType
import com.codeIn.common.data.Code
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountState
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.CheckCodeRepository
import com.codeIn.myCash.features.user.domain.authentication.usecases.CheckCodeValidationUseCase
import javax.inject.Inject

class CheckCodeRepositoryImpl @Inject constructor (private var authentication: Authentication,
                                                    private val prefs : SharedPrefsModule,
                                                    private val errorHandler: ErrorHandlerImpl,
                                                    private val checkCodeValidationUseCase: CheckCodeValidationUseCase ) : CheckCodeRepository{
    override suspend fun checkCode(
        otp : String? ,
        phone: String?,
        countryId: String?,
        type: String,
        email: String?,
        active: String?,
        key : String?
    ) : AuthenticationState {
        return try{

            val lang  = prefs.getValue(Constants.getLang())

            checkCodeValidationUseCase.invoke(otp).let {
                if (it != InvalidInput.NONE) return AuthenticationState.InputError(it)
            }
            authentication.checkCode(
                lang= lang ,
                code= otp ,
                phone = phone,
                countryId = countryId ,
                type = type,
                email = email,
                active = active,
            ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        if (key == Code.FORGET.value.toString()){
                            prefs.putValue(Constants.forgetToken() , "Bearer "+response.body()?.data?.token)
                        }
                        else if (key == Code.REGISTER.value.toString()){
                            prefs.putValue(Constants.registerToken() , "Bearer "+response.body()?.data?.token)
                        }
                        AuthenticationState.Success(response.body()?.data)
                    }
                    else {
                        AuthenticationState.StateError(response.body()?.message)
                    }
                }
                else if (response.code() == 401){
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