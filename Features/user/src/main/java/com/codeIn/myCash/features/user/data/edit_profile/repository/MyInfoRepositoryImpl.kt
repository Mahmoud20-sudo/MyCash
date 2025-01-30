package com.codeIn.myCash.features.user.data.edit_profile.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.ProfileState
import com.codeIn.myCash.features.user.data.edit_profile.remote.ApiEditProfile
import com.codeIn.myCash.features.user.domain.edit_profile.repository.MyInfoRepository
import javax.inject.Inject

class MyInfoRepositoryImpl @Inject constructor(
    private val api: ApiEditProfile,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : MyInfoRepository {
    override suspend fun myInfo(): ProfileState<UserDTO> {
        return try {

            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val response = api.profile(lang, token)

            when {
                response.isSuccessful && response.body()?.status == 1 -> {
                    response.body()?.let {
                        ProfileState.Success(it)
                    } ?: run {
                        ProfileState.StateError(response.body()?.message.toString())
                    }
                }
                else -> ProfileState.ServerError(errorHandler.invoke(response.code()))
            }
        } catch (throwable: Throwable) {
            ProfileState.ServerError(errorHandler.getError(throwable))
        }
    }
}