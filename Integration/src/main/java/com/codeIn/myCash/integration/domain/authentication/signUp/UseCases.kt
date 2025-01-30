package com.codeIn.myCash.integration.domain.authentication.signUp

import javax.inject.Inject


class PackagesUseCase @Inject constructor(
    private val repository: PackagesRepository
) {
    suspend operator fun invoke() = repository.getPackages()
}


class DevicesUseCase @Inject constructor(
    private val repository: DevicesRepository
) {
    suspend operator fun invoke() = repository.getDevices()
}

class RegisterUseCase @Inject constructor(
    private val repository: RegisterRepository
) {
    suspend operator fun invoke(
        packageId: Int,
        deviceId: Int,
        countryId: Int,
        phoneNumber: String,
        password: String,
        email: String,
        influencerId: String
    ) = repository.register(
        packageId = packageId,
        deviceId = deviceId,
        countryId = countryId,
        phoneNumber = phoneNumber,
        password = password,
        email = email,
        influencerId = influencerId
    )
}


class OtpUseCase @Inject constructor(
    private val repository: OtpRepository
) {
    suspend fun checkOtpCode(
        phoneNumber: String? = null,
        email: String? = null,
        otpCode: String,
        countryId: Int
    ) = repository.checkOtpCode(
        phoneNumber = phoneNumber,
        email = email,
        otpCode = otpCode,
        countryId = countryId
    )

    suspend fun resendOtpCode(
        phoneNumber: String? = null,
        email: String? = null,
        countryId: Int
    ) = repository.resendOtpCode(phoneNumber = phoneNumber, email = email, countryId = countryId)
}