package com.codeIn.myCash.features.user.data.authentication.repository
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.printer.DeviceIntegration
import com.codeIn.common.printer.getManufacturerInfo
import com.codeIn.common.printer.isInEnum
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.domain.authentication.repository.LoginRepository
import com.codeIn.myCash.features.user.domain.authentication.usecases.LoginValidationUseCase
import javax.inject.Inject

class LoginRepositoryImpl
    @Inject constructor (private var authentication: Authentication,
                         private val prefs : SharedPrefsModule,
                         private val errorHandler: ErrorHandlerImpl,
                         private val loginValidationUseCase: LoginValidationUseCase): LoginRepository{

    override suspend fun login(
        phone: String,
        countryId: String,
        password: String,
    ): AuthenticationState {


        return try{

            val lang  = prefs.getValue(Constants.getLang())
            val hasPhone : String = if (isInEnum(getManufacturerInfo() , DeviceIntegration::class.java))
                "0"
            else
                "1"

            loginValidationUseCase.invoke(phoneNumberSaudi = phone, password = password).let {
                if (it != InvalidInput.NONE) return AuthenticationState.InputError(it)
            }

            authentication.login(lang , phone , password , hasPhone , countryId , "").let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1 ) {
                        val user = response.body()?.data
                        if (user?.status == "1"){
                            prefs.putValue(Constants.getToken() , "Bearer "+user?.token)
                            prefs.putValue(Constants.getCurrency() , user?.country?.currency)
                            prefs.putValue(Constants.logoStore() , user?.accountInfo?.logo)
                            prefs.putValue(Constants.nameStore() , user?.accountInfo?.commercialRecordName)
                            prefs.putValue(Constants.completeInoStore() , user?.isCompleteAccountInfo)
                            prefs.putValue(Constants.paymentStatus() , user?.paymentStatus)
                        }
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