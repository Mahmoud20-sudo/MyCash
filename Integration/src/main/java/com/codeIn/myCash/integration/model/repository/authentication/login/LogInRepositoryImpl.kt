package com.codeIn.myCash.integration.model.repository.authentication.login

import android.util.Log
import com.codeIn.myCash.integration.domain.authentication.login.LogInRepository
import com.codeIn.myCash.integration.model.dataClasses.authentication.UserState
import com.codeIn.myCash.integration.model.remoteSource.authentication.login.LogInRemoteSource
import com.codeIn.myCash.integration.utilities.InvalidInput.NONE
import com.codeIn.myCash.integration.utilities.Validation
import javax.inject.Inject

class LogInRepositoryImpl @Inject constructor(
    private val remoteSource: LogInRemoteSource,
    private val validation: Validation,
) : LogInRepository {
    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }
    override suspend fun logIn(phone: String, password: String, countryId: Int): UserState {

        validation.validate(phoneNumberSaudi = phone, password = password)
            .let { if (it != NONE) return UserState.InputError(it) }

        return try {
            val res = remoteSource.logIn(
                phone = phone,
                password = password,
                countryId = countryId
            )
            if (res.status == 1) res.data?.let { UserState.Success(it) }
                ?: UserState.Error(res.message ?: "An error occurred")
            else UserState.Error(res.message ?: "An error occurred")
        } catch (e: Exception) {
            Log.e(debugTAG, "logIn: ${e.message}")
            UserState.Error(e.message ?: "An error occurred")
        }
    }
}