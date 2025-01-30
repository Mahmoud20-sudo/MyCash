package com.codeIn.common.domain.usecases

import javax.inject.Inject

class MoneyValidationUseCase @Inject constructor(){

    operator fun invoke(money : String?): Boolean{
        val value = if (money.isNullOrBlank())
            0.0
        else
            money.toDouble() ?: 0.0
        return value >= 0
    }
}