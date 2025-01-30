package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class ResetPasswordValidationUseCase @Inject constructor(
    private val passwordUseCase: ValidatePasswordUseCase,
    private val emptyUseCase: ValidateEmptyUseCase,
    private val matchPasswordUseCase: MatchPasswordUseCase) {

    operator fun invoke( password: String?, confirmPassword: String? ): InvalidInput {
        return when {
            emptyUseCase.invoke(password) -> InvalidInput.EMPTY
            !passwordUseCase.invoke(password) -> InvalidInput.PASSWORD
            emptyUseCase.invoke(confirmPassword) -> InvalidInput.EMPTY
            !passwordUseCase.invoke(confirmPassword) -> InvalidInput.CONFIRM_PASSWORD
            !matchPasswordUseCase.invoke(password , confirmPassword) -> InvalidInput.MATCH
            else -> InvalidInput.NONE
        }
    }
}