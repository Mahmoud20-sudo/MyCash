package com.codeIn.myCash.features.sales.domain.expenses.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.MoneyValidationUseCase
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class CreateExpenseValidationUseCase @Inject constructor(
    private val emptyUseCase: ValidateEmptyUseCase,
    private val moneyValidationUseCase: MoneyValidationUseCase
) {
    operator fun invoke(statement: String?, amount: String?, date: String?): InvalidInput {
        return when {
            emptyUseCase.invoke(statement) -> InvalidInput.EMPTY
            emptyUseCase.invoke(amount) -> InvalidInput.EMPTY
            !moneyValidationUseCase.invoke(amount) -> InvalidInput.PRICE
            emptyUseCase.invoke(date) -> InvalidInput.EMPTY
            else -> InvalidInput.NONE
        }
    }
}