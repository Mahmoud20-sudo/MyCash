package com.codeIn.myCash.features.user.data.users.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.users.model.response.DeleteDTO
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.data.users.remote.ApiUsers
import com.codeIn.myCash.features.user.domain.users.repository.DeleteAllEmployeeRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class DeleteAllEmployeeRepositoryImpl @Inject constructor(
    private val api: ApiUsers,
    private val errorHandler: ErrorHandlerImpl,
    private val prefs: SharedPrefsModule
): DeleteAllEmployeeRepository{
    override suspend fun deleteAllEmployee(deleteAll: Int): UsersState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val response = api.deleteAllUser(lang = lang, authorization = token, deleteAll)

            if (response.isSuccessful) {
                when(response.body()?.status){
                    1 -> {
                        val data = response.body()
                        data?.let {
                            UsersState.SuccessDeleteAllUsers(data)
                        } ?: UsersState.StateError(response.message())
                    }
                    else -> UsersState.StateError(response.body()?.message)
                }
            } else {
                UsersState.ServerError(errorHandler.invoke(response.code()))
            }

        }catch (throwable: Throwable) {
            UsersState.ServerError(errorHandler.getError(throwable))
        }
    }
}