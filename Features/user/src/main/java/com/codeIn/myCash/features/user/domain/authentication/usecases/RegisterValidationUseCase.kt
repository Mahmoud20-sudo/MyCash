package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.ValidateEmailUseCase
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import com.codeIn.common.domain.usecases.ValidatePhoneSaudiUseCase
import javax.inject.Inject

class RegisterValidationUseCase @Inject constructor(
    private val phoneSaudiUseCase: ValidatePhoneSaudiUseCase,
    private val passwordUseCase: ValidatePasswordUseCase,
    private val emptyUseCase: ValidateEmptyUseCase,
    private val emailUseCase: ValidateEmailUseCase
){
    operator fun invoke( phoneNumberSaudi: String?, password: String? , email : String?): InvalidInput {
        return when {
            emptyUseCase.invoke(email) -> InvalidInput.EMPTY
            !emailUseCase.invoke(email) -> InvalidInput.EMAIL
            emptyUseCase.invoke(phoneNumberSaudi) -> InvalidInput.EMPTY
            !phoneSaudiUseCase.invoke(phoneNumberSaudi) -> InvalidInput.PHONE_SAUDI
            emptyUseCase.invoke(password) -> InvalidInput.EMPTY
            !passwordUseCase.invoke(password) -> InvalidInput.PASSWORD
            else -> InvalidInput.NONE
        }
    }
}