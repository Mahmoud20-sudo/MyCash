package com.codeIn.myCash.features.user.data.settings.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.settings.remote.GeneralData
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.PackagesState
import com.codeIn.myCash.features.user.domain.settings.repository.PackagesRepository
import javax.inject.Inject

class PackagesRepositoryImpl @Inject constructor(private val generalData: GeneralData,
                                                 private val prefs : SharedPrefsModule,
                                                 private val errorHandler: ErrorHandlerImpl) : PackagesRepository{
    override suspend fun getPackages(countryId: String?): PackagesState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            generalData.getPackages(lang = lang , countryId = countryId).let {response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        PackagesState.Success(response.body()?.data?.data)
                    } else {
                        PackagesState.StateError(response.body()?.message)
                    }
                }else{
                    PackagesState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            return PackagesState.ServerError(errorHandler.getError(throwable))
        }
    }


}