package com.codeIn.myCash.features.stock.domain.product.usecases

import com.codeIn.common.data.NumberHelper
import javax.inject.Inject

class ValueValidationUseCase @Inject constructor(){

    operator fun invoke(discount : String? , finalPrice:String?): Boolean {
        if (NumberHelper.persianToEnglishText(discount?:"0.0").toDouble()
            >= NumberHelper.persianToEnglishText(finalPrice?:"0.0").toDouble())
            return false

        return true
    }
}