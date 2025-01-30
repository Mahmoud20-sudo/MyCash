package com.codeIn.myCash.features.user.data.users.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.users.model.response.CreateUserResponse
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.data.users.remote.ApiUsers
import com.codeIn.myCash.features.user.domain.users.repository.CreateUsersRepository
import javax.inject.Inject

class CreateUserRepositoryImpl @Inject constructor(
    private val api: ApiUsers,
    private val errorHandler: ErrorHandlerImpl,
    private val prefs: SharedPrefsModule
) : CreateUsersRepository {
    override suspend fun createUser(
        name: String,
        email: String,
        phone: String?,
        password: String,
        status: Int,
        branchId: String?,
        type: String?
    ): UsersState {
        return try {
            val token = prefs.getValue(Constants.getToken())
            val lang = prefs.getValue(Constants.getLang())
            val response = api.createUsers(
                lang =lang,
                authorization = token,
                name = name,
                email = email,
                phone = phone,
                password = password,
                status = status,
                branchId = branchId ,
                type = type
            )


            if (response.isSuccessful && response.body()?.status == 1) {
                val data = response.body()
                data?.let {
                    UsersState.SuccessCreateUser(data)
                } ?: UsersState.StateError(response?.message())
            } else {
                UsersState.ServerError(errorHandler.invoke(response.code()))
            }
        } catch (throwable: Throwable) {
            Log.e("CreateUserRepositoryImpl", "Error creating user: ${throwable.message}", throwable)
            UsersState.ServerError(errorHandler.getError(throwable))
        }
    }
}
