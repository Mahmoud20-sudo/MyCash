package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.ResetPasswordRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val resetPasswordRepository : ResetPasswordRepository) {
    suspend operator fun invoke(
        password : String?,
        confirmPassword : String? ,
    ): AuthenticationState {
        return resetPasswordRepository.resetPassword(password , confirmPassword )
    }
}