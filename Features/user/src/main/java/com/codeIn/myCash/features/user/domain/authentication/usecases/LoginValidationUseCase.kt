package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import com.codeIn.common.domain.usecases.ValidatePhoneSaudiUseCase
import javax.inject.Inject

class LoginValidationUseCase @Inject constructor(
    private val phoneSaudiUseCase: ValidatePhoneSaudiUseCase,
    private val passwordUseCase: ValidatePasswordUseCase,
    private val emptyUseCase: ValidateEmptyUseCase
){
    operator fun invoke( phoneNumberSaudi: String?, password: String? ): InvalidInput {
        return when {
            emptyUseCase.invoke(phoneNumberSaudi) -> InvalidInput.EMPTY
            !phoneSaudiUseCase.invoke(phoneNumberSaudi) -> InvalidInput.PHONE_SAUDI
            emptyUseCase.invoke(password) -> InvalidInput.EMPTY
            !passwordUseCase.invoke(password) -> InvalidInput.PASSWORD
            else -> InvalidInput.NONE
        }
    }
}