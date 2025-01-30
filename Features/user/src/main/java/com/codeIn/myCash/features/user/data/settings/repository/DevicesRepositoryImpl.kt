package com.codeIn.myCash.features.user.data.settings.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DevicesState
import com.codeIn.myCash.features.user.data.settings.remote.GeneralData
import com.codeIn.myCash.features.user.domain.settings.repository.DevicesRepository
import javax.inject.Inject

class DevicesRepositoryImpl @Inject constructor(private val generalData: GeneralData,
                                                private val prefs : SharedPrefsModule,
                                                private val errorHandler: ErrorHandlerImpl) : DevicesRepository{
    override suspend fun getDevices(countryId: String): DevicesState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            generalData.getDevices(lang = lang , countryId = countryId).let {response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        DevicesState.Success(response.body()?.data)
                    } else {
                        DevicesState.StateError(response.body()?.message)
                    }
                }else{
                    DevicesState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            return DevicesState.ServerError(errorHandler.getError(throwable))
        }
    }
}