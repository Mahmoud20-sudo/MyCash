package com.codeIn.myCash.features.user.data.users.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO
import com.codeIn.myCash.features.user.data.users.model.response.UsersResponse
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.data.users.remote.ApiUsers
import com.codeIn.myCash.features.user.domain.users.repository.UserDetailsRepository
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import javax.inject.Inject

class UserDetailsRepositoryImpl @Inject constructor(
    private val api:ApiUsers,
    private val errorHandler: ErrorHandlerImpl,
    private val prefs: SharedPrefsModule
) : UserDetailsRepository{
    override suspend fun userDetails(employeeId: Int): UsersState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val response = api.getUserDetails(
                lang = lang,
                authorization  = token,
                employeeId = employeeId)

            if (response.isSuccessful && response.body()?.status == 1) {
                val data = response.body()
                data?.let {
                    UsersState.SuccessRetrieveUsers(data)
                } ?: UsersState.StateError(response?.message())
            } else {
                UsersState.ServerError(errorHandler.invoke(response.code()))
            }
        }catch (throwable: Throwable){
//            Response.error(500, ResponseBody.create(null, "Internal Server Error"))
            UsersState.ServerError(errorHandler.invoke(HTTP_INTERNAL_ERROR))

        }
    }
}