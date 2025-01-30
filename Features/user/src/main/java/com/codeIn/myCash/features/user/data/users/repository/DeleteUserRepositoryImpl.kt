package com.codeIn.myCash.features.user.data.users.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.users.model.response.DeleteDTO
import com.codeIn.myCash.features.user.data.users.model.response.UsersResponse
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.data.users.remote.ApiUsers
import com.codeIn.myCash.features.user.domain.users.repository.DeleteUserRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class DeleteUserRepositoryImpl @Inject constructor(
    private val api: ApiUsers,
    private val errorHandler: ErrorHandlerImpl,
    private val prefs: SharedPrefsModule) : DeleteUserRepository{
    override suspend fun deleteUser(employeeId: Int): UsersState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val response=  api.deleteUser(
                lang = lang,
                authorization  = token,
                employeeId = employeeId)

            if (response.isSuccessful) {
                when(response.body()?.status){
                    1 -> {
                        val data = response.body()
                        data?.let {
                            UsersState.SuccessDeleteSingleUser(data)
                        } ?: UsersState.StateError(response.message())
                    }
                    else -> UsersState.StateError(response.body()?.message)
                }
            } else {
                UsersState.ServerError(errorHandler.invoke(response.code()))
            }

        }catch (throwable: Throwable) {
            // In case of exception, return an error response
            UsersState.ServerError(errorHandler.getError(throwable))
        }
    }
}