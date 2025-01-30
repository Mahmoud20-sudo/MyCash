package com.codeIn.myCash.features.user.data.shift.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.shift.remote.Shift
import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftState
import com.codeIn.myCash.features.user.domain.shift.repository.ShiftRepository
import com.codeIn.myCash.features.user.domain.shift.usecases.ShiftValidationUseCase
import javax.inject.Inject

class ShiftRepositoryImpl @Inject constructor(private val shift: Shift,
                                              private val prefs : SharedPrefsModule,
                                              private val errorHandler: ErrorHandlerImpl,
                                              private val shiftValidationUseCase: ShiftValidationUseCase
): ShiftRepository{
    override suspend fun currentShift(): ShiftState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())
            shift.currentShift(lang = lang , authorization = token).let {response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        ShiftState.CurrentShift(response.body()?.data)
                    } else {
                        ShiftState.StateError(response.body()?.message)
                    }
                }
                else if (response.code() == 401){
                    ShiftState.UnAuthorized
                }else{
                    ShiftState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            return ShiftState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun startShift(
        cash: String?, visa: String?, differentCash: String?, differentVisa: String?
    ): ShiftState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

            shiftValidationUseCase.invoke(cash = cash , visa= visa ).let {
                if (it != InvalidInput.NONE) return ShiftState.InputError(it)
            }

            shift.startShift(lang = lang , authorization = token , startCash = cash ,
                startVisa = visa , differentInCash = differentCash , differentInVisa = differentVisa).let {response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        ShiftState.StartShift(response.body()?.data)
                    } else {
                        ShiftState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401){
                    ShiftState.UnAuthorized
                }else{
                    ShiftState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            return ShiftState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun endShift(visa: String?, cash: String?): ShiftState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

            shiftValidationUseCase.invoke(cash = cash , visa= visa  ).let {
                if (it != InvalidInput.NONE) return ShiftState.InputError(it)
            }

            shift.logout(lang = lang , authorization = token , endCash = cash , endVisa = visa).let {response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        prefs.putValue(Constants.getToken() , "")
                        prefs.putValue(Constants.getCurrency() , "")
                        prefs.putValue(Constants.logoStore() , "")
                        prefs.putValue(Constants.nameStore() ,"")
                        ShiftState.EndShift(response.body()?.message)
                    } else {
                        ShiftState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401){
                    ShiftState.UnAuthorized
                }else{
                    ShiftState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            return ShiftState.ServerError(errorHandler.getError(throwable))
        }
    }
}