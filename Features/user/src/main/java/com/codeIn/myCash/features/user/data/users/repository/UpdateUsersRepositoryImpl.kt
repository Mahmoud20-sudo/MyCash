package com.codeIn.myCash.features.user.data.users.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.users.model.response.UsersResponse
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.data.users.remote.ApiUsers
import com.codeIn.myCash.features.user.domain.users.repository.UpdateUsersRepository
import javax.inject.Inject

class UpdateUsersRepositoryImpl @Inject constructor(
    private val api : ApiUsers,
    private val errorHandler: ErrorHandlerImpl,
    private val prefs: SharedPrefsModule
) : UpdateUsersRepository{
    override suspend fun updateUsers(
        name: String,
        email: String,
        phone: String?,
        password: String,
        note: String,
        employeeId: Int,
        branchId: String? ,
        type : String?
    ): UsersState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val response = api.updateUsers(
                lang = lang,
                authorization = token,
                name = name,
                email = email,
                phone = phone,
                password = password,
                note = note,
                employeeID = employeeId,
                branchId = branchId ,
                type = type
            )

            if (response.isSuccessful && response.body()?.status == 1) {
                val data = response.body()
                data?.let {
                    UsersState.SuccessRetrieveUsers(data)
                } ?: UsersState.StateError(response?.message())
            } else {
                UsersState.ServerError(errorHandler.invoke(response.code()))
            }

        } catch (throwable: Throwable) {
            UsersState.ServerError(errorHandler.getError(throwable))
        }
    }
}