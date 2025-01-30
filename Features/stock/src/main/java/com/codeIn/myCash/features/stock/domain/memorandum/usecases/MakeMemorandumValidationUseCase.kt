package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.MoneyValidationUseCase
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class MakeMemorandumValidationUseCase @Inject constructor(
    private val emptyUseCase: ValidateEmptyUseCase ,
    private val moneyValidationUseCase: MoneyValidationUseCase,
){
    operator fun invoke( price : String? , quantity : String?) : InvalidInput {
        return when {
            emptyUseCase.invoke(price) -> InvalidInput.EMPTY
            !moneyValidationUseCase.invoke(price) ->InvalidInput.PRICE
            !moneyValidationUseCase.invoke(quantity) -> InvalidInput.QUANTITY
            else -> InvalidInput.NONE
        }
    }
}