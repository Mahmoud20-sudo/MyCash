package com.codeIn.myCash.features.stock.domain.product.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.BuyPriceValidationUseCase
import com.codeIn.common.domain.usecases.MoneyValidationUseCase
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class CreateProductValidationUseCase @Inject constructor(
    private val emptyUseCase: ValidateEmptyUseCase ,
    private val moneyValidationUseCase: MoneyValidationUseCase,
    private val buyPriceValidationUseCase: BuyPriceValidationUseCase
){
    operator fun invoke(name : String? , price : String? , quantity : String? , buyPrice : String?) : InvalidInput {
        return when {
            emptyUseCase.invoke(name) -> InvalidInput.EMPTY
            emptyUseCase.invoke(price) -> InvalidInput.EMPTY
            !moneyValidationUseCase.invoke(price) ->InvalidInput.PRICE
//            emptyUseCase.invoke(buyPrice) -> InvalidInput.EMPTY
//            !moneyValidationUseCase.invoke(buyPrice) ->InvalidInput.BUY_PRICE
            !buyPriceValidationUseCase.invoke(price , buyPrice) ->InvalidInput.BUY_PRICE_SMALL
            !moneyValidationUseCase.invoke(quantity) -> InvalidInput.QUANTITY
            else -> InvalidInput.NONE
        }
    }
}