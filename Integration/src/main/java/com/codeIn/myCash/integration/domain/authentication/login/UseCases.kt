package com.codeIn.myCash.integration.domain.authentication.login

import javax.inject.Inject


class LogInUseCase @Inject constructor(
    private val repository: LogInRepository
) {
    suspend operator fun invoke(phone: String, password: String, countryId: Int) =
        repository.logIn(phone, password, countryId)
}


class ForgotPasswordUseCase @Inject constructor(
    private val repository: ForgotPasswordRepository
) {
    suspend operator fun invoke(
        phone: String? = null,
        email: String? = null,
        countryId: Int
    ) = repository.forgotPassword(phone = phone, email = email, countryId = countryId)
}

class ResetPasswordUseCase @Inject constructor(
    private val repository: ResetPasswordRepository
) {
    suspend operator fun invoke(password: String, confirmPassword: String) =
        repository.resetPassword(password, confirmPassword)
}