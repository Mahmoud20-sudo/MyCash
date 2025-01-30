package com.codeIn.common.calculator

import com.codeIn.common.data.AppConstants
import com.codeIn.common.offline.SharedPrefsModule
import javax.inject.Inject

class TaxCalculator @Inject constructor(
    private val prefsModule: SharedPrefsModule
){


    fun calculateTaxValue(total : String?) : String{
        val tax = prefsModule.getValue(AppConstants.TAX)?:"0.0"
        return (total?.toDouble()!! * tax.toDouble() /100).toString()
    }
}