package com.codeIn.myCash.features.user.data.users.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.data.users.remote.ApiUsers
import com.codeIn.myCash.features.user.domain.users.repository.UsersRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.internal.http.HTTP_UNAVAILABLE
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiUsers,
    private val errorHandler: ErrorHandlerImpl,
    private val prefs: SharedPrefsModule
): UsersRepository {
    override suspend fun getUser(searchText: String?): UsersState {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())
        return if (!token.isNullOrEmpty()) {
            val response = api.getUsers(lang,token,searchText)

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
            UsersState.ServerError(errorHandler.invoke(HTTP_UNAUTHORIZED))
        }
    }
}