package com.codeIn.myCash.features.user.data.users.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.data.users.remote.ApiUsers
import com.codeIn.myCash.features.user.domain.users.repository.RefreshRepository
import com.codeIn.myCash.features.user.domain.users.repository.UsersRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class RefreshUserRepositoryImpl @Inject constructor(
    private val api: ApiUsers,
    private val errorHandler: ErrorHandlerImpl,
    private val prefs: SharedPrefsModule
): RefreshRepository {

    override suspend fun refreshUser(): UsersState {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())
        return if (!token.isNullOrEmpty()) {
            val response = api.getRefresh(lang,token)
            if (response.isSuccessful && response.body()?.status == 1) {
                val data = response.body()
                data?.let {
                    UsersState.SuccessRetrieveUsers(data)
                } ?: UsersState.StateError(response?.message())
            } else {
                UsersState.ServerError(errorHandler.invoke(response.code()))
            }
        } else {
//            Response.error(401, ResponseBody.create("application/json".toMediaTypeOrNull(), ""))
            UsersState.ServerError(errorHandler.invoke(HttpURLConnection.HTTP_UNAUTHORIZED))
        }
    }
}