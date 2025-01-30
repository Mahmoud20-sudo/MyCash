package com.codeIn.myCash.features.user.data.complete_data.repository

import android.util.Log
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.Validation
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import com.codeIn.myCash.features.user.data.complete_data.model.CompleteInfoState
import com.codeIn.myCash.features.user.data.complete_data.remote.ApiCompleteData
import com.codeIn.myCash.features.user.data.edit_profile.remote.ApiEditProfile
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.complete_data.repository.CompleteInfoRepository
import com.codeIn.myCash.features.user.domain.complete_data.repository.LogoutRepository
import com.codeIn.myCash.features.user.domain.edit_profile.repository.UpdateProfileRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class LogoutRepositoryImpl @Inject constructor(
    private val api: ApiCompleteData,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : LogoutRepository {
    override suspend fun logout(): CompleteInfoState = try {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())

        val response = api.logout(
            authorization = token,
            lang = lang
        )

        if (response.isSuccessful && response.body()?.status == 1) {
            val data = response.body()
            data?.let {
                CompleteInfoState.SuccessLogout(data)
            } ?: CompleteInfoState.StateError(response?.message())
        } else {
            CompleteInfoState.ServerError(errorHandler.invoke(response.code()))
        }
    } catch (throwable: Throwable) {
        CompleteInfoState.ServerError(errorHandler.getError(throwable))
    }
}
