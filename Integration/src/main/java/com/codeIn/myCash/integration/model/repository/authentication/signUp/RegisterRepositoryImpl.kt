package com.codeIn.myCash.integration.model.repository.authentication.signUp

import android.util.Log
import com.codeIn.myCash.integration.domain.authentication.signUp.RegisterRepository
import com.codeIn.myCash.integration.model.dataClasses.authentication.UserState
import com.codeIn.myCash.integration.model.network.NetworkParams
import com.codeIn.myCash.integration.model.remoteSource.authentication.signUp.RegisterRemoteSource
import com.codeIn.myCash.integration.utilities.InvalidInput
import com.codeIn.myCash.integration.utilities.Validation
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RegisterRepositoryImpl @Inject constructor(
    private val remoteSource: RegisterRemoteSource,
    private val validation: Validation,
) :
    RegisterRepository {

    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }
    private val success = NetworkParams.SUCCESS_STATUS

    override suspend fun register(
        packageId: Int,
        deviceId: Int,
        countryId: Int,
        phoneNumber: String,
        password: String,
        email: String,
        influencerId: String
    ): UserState {

        validation.validate(
            email = email,
            password = password,
            phoneNumberSaudi = phoneNumber
        ).let {
            if (it != InvalidInput.NONE) return UserState.InputError(it)
        }

        return try {
            val res = remoteSource.register(
                packageId = packageId,
                deviceId = deviceId,
                countryId = countryId,
                phoneNumber = phoneNumber,
                password = password,
                email = email,
                influencerId = influencerId
            )
            if (res.status == success) res.data?.let { UserState.Success(it) }
                ?: UserState.Error("User not found")
            else UserState.Error(res.message ?: "An error occurred")
        } catch (e: Exception) {
            Log.e(debugTAG, "register: ${e.message}")
            UserState.Error(e.message ?: "An error occurred")
        }
    }
}