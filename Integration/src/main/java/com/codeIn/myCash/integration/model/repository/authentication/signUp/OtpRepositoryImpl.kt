package com.codeIn.myCash.integration.model.repository.authentication.signUp

import android.util.Log
import com.codeIn.myCash.integration.domain.authentication.signUp.OtpRepository
import com.codeIn.myCash.integration.model.dataClasses.authentication.OtpState
import com.codeIn.myCash.integration.model.remoteSource.authentication.signUp.OtpRemoteSource
import javax.inject.Inject

class OtpRepositoryImpl @Inject constructor(
    private val remoteSource: OtpRemoteSource
) : OtpRepository {
    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }
    override suspend fun checkOtpCode(
        phoneNumber: String?,
        email: String?,
        otpCode: String,
        countryId: Int
    ): OtpState {
        return try {
            val res = remoteSource.checkCode(
                phone = phoneNumber,
                email = email,
                code = otpCode,
                countryId = countryId
            )
            if (res.status == 1) OtpState.CheckCodeSuccess(res.message ?: "success")
            else OtpState.Error(res.message ?: "An error occurred")
        } catch (e: Exception) {
            Log.e(debugTAG, "checkOtpCode: ${e.message}")
            OtpState.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun resendOtpCode(
        phoneNumber: String?,
        email: String?,
        countryId: Int
    ): OtpState {
        val res = remoteSource.resendCode(
            phone = phoneNumber,
            email = email,
            countryId = countryId
        )
        return try {
            if (res.status == 1) OtpState.ResendCodeSuccess(res.message ?: "success")
            else OtpState.Error(res.message ?: "An error occurred")
        } catch (e: Exception) {
            Log.e(debugTAG, "resendOtpCode: ${e.message}")
            OtpState.Error(e.message ?: "An error occurred")
        }
    }
}