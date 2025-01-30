package com.codeIn.myCash.features.user.domain.shift.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.MoneyValidationUseCase
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class ShiftValidationUseCase @Inject constructor(
    private val emptyUseCase: ValidateEmptyUseCase,
) {

    operator fun invoke( cash: String?, visa: String? ): InvalidInput {
        return when {
            emptyUseCase.invoke(cash) -> InvalidInput.EMPTY
            emptyUseCase.invoke(visa) -> InvalidInput.EMPTY
            else -> InvalidInput.NONE
        }
    }
}