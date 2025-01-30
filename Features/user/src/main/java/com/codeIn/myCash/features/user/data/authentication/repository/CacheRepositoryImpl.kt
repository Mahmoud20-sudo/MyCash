package com.codeIn.myCash.features.user.data.authentication.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.domain.authentication.repository.CacheRepository
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor (private val prefs : SharedPrefsModule,
                                               private val errorHandler: ErrorHandlerImpl,
): CacheRepository {
    override suspend fun saveCache(phone: String?, password: String?): GeneralResponse {
        return try{

            prefs.putValue(Constants.getPhone() , phone)
            prefs.putValue(Constants.getPassword() , password)
            prefs.putValue(Constants.isRememberMe() , "1")
            GeneralResponse.Success(null)
        }catch (throwable : Throwable){
            GeneralResponse.ServerError(errorHandler.getError(throwable))
        }
    }

}