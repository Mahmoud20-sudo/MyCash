package com.codeIn.common.domain.usecases

import java.util.regex.Pattern
import javax.inject.Inject

class ValidateCommercialOrTaxRecordUseCase  @Inject constructor(){
    fun commercial(data : String?): Boolean{
        val pattern = Pattern.compile("[0-9]{10}$")

        return pattern.matcher(data!!).matches()
    }
    fun taxRecord(data : String?): Boolean{
        val pattern = Pattern.compile("[0-9]{15}$")

        return pattern.matcher(data!!).matches()
    }
}