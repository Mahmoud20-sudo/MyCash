package com.codeIn.common.domain.usecases

import javax.inject.Inject

class BuyPriceValidationUseCase @Inject constructor(){

    operator fun invoke(price : String? , buyPrice : String?): Boolean{
        val priceValue = if (price.isNullOrBlank())
            0.0
        else
            price.toDouble() ?: 0.0

        val buyPriceValue = if (buyPrice.isNullOrBlank())
            0.0
        else
            buyPrice.toDouble() ?: 0.0

        return priceValue >= buyPriceValue
    }
}