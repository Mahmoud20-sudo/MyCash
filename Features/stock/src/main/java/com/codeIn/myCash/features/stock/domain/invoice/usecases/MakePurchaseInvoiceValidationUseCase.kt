package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class MakePurchaseInvoiceValidationUseCase @Inject constructor(
    private val emptyUseCase: ValidateEmptyUseCase ) {

    operator fun invoke(
        referenceNumber : String?,
        referenceDate : String?,
    ): InvalidInput{
        return when {
            emptyUseCase.invoke(referenceNumber) -> InvalidInput.EMPTY
            emptyUseCase.invoke(referenceDate) -> InvalidInput.EMPTY
            else -> InvalidInput.NONE
        }
    }
}