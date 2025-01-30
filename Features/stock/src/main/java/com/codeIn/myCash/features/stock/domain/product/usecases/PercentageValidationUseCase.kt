package com.codeIn.myCash.features.stock.domain.product.usecases

import com.codeIn.common.data.NumberHelper
import javax.inject.Inject

class PercentageValidationUseCase @Inject constructor(){

    operator fun invoke(percentage : String?): Boolean{
        if (NumberHelper.persianToEnglishText(percentage?:"0.0").toDouble() < 0.0
            || NumberHelper.persianToEnglishText(percentage ?:"0.0").toDouble() >= 100)
            return false

        return true
    }
}