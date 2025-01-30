package com.codeIn.myCash.integration.model.repository.authentication.login

import android.util.Log
import com.codeIn.myCash.integration.domain.authentication.login.ForgotPasswordRepository
import com.codeIn.myCash.integration.domain.authentication.login.ResetPasswordRepository
import com.codeIn.myCash.integration.model.dataClasses.authentication.ForgotPasswordState
import com.codeIn.myCash.integration.model.dataClasses.authentication.ResetPasswordState
import com.codeIn.myCash.integration.model.remoteSource.authentication.login.ForgotPasswordRemoteSource
import com.codeIn.myCash.integration.utilities.InvalidInput.NONE
import com.codeIn.myCash.integration.utilities.Validation
import javax.inject.Inject

class ForgotPasswordRepositoryImpl @Inject constructor(
    private val remoteSource: ForgotPasswordRemoteSource,
    private val validation: Validation,
) : ForgotPasswordRepository, ResetPasswordRepository {
    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }
    override suspend fun forgotPassword(
        phone: String?,
        email: String?,
        countryId: Int
    ): ForgotPasswordState {

        validation.validate(phoneNumberSaudi = phone, email = email)
            .let { if (it != NONE) return ForgotPasswordState.InputError(it) }

        return try {
            val res = remoteSource.forgotPassword(
                phone = phone,
                email = email,
                countryId = countryId
            )
            if (res.status == 1) ForgotPasswordState.Success(res.message ?: "success")
            else ForgotPasswordState.Error(res.message ?: "An error occurred")
        } catch (e: Exception) {
            Log.e(debugTAG, "forgotPassword: ${e.message}")
            ForgotPasswordState.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun resetPassword(
        password: String,
        confirmPassword: String
    ): ResetPasswordState {

        validation.validate(password = password, confirmPassword = confirmPassword)
            .let { if (it != NONE) return ResetPasswordState.InputError(it) }

        return try {
            val res = remoteSource.resetPassword(password)
            if (res.status == 1) ResetPasswordState.Success(res.message ?: "success")
            else ResetPasswordState.Error(res.message ?: "An error occurred")
        } catch (e: Exception) {
            Log.e(debugTAG, "resetPassword: ${e.message}")
            ResetPasswordState.Error(e.message ?: "An error occurred")
        }
    }
}