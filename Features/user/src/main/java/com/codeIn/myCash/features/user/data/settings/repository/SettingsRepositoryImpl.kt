package com.codeIn.myCash.features.user.data.settings.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.settings.remote.GeneralData
import com.codeIn.myCash.features.user.data.settings.remote.response.settings.SettingsState
import com.codeIn.myCash.features.user.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val generalData: GeneralData,
                                                 private val prefs : SharedPrefsModule,
                                                 private val errorHandler: ErrorHandlerImpl) : SettingsRepository{
    override suspend fun getSettings(): SettingsState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            generalData.getSettings(lang ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        SettingsState.Success(response.body()?.data)
                    } else {
                        SettingsState.StateError(response.body()?.message)
                    }
                }else{
                    SettingsState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            return SettingsState.ServerError(errorHandler.getError(throwable))
        }
    }
}