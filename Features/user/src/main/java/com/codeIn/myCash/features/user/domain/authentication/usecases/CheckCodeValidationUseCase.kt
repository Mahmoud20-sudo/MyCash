package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class CheckCodeValidationUseCase @Inject constructor(private val emptyUseCase: ValidateEmptyUseCase,) {
    operator fun invoke( otp : String?) : InvalidInput{
        return when {
            emptyUseCase.invoke(otp) -> InvalidInput.EMPTY
            else -> InvalidInput.NONE
        }
    }
}