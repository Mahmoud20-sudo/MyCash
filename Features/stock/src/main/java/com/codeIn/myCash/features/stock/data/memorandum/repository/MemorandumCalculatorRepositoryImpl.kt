package com.codeIn.myCash.features.stock.data.memorandum.repository

import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.Taxable
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.toTwoDigits
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.repository.MemorandumCalculatorRepository
import javax.inject.Inject

class MemorandumCalculatorRepositoryImpl @Inject constructor(
    private val pref : SharedPrefsModule
) : MemorandumCalculatorRepository{
    override suspend fun makeCalculationOfInvoice(products: ProductInInvoiceMemorandum?): ProductInInvoiceMemorandum? {
        val taxSystem = NumberHelper.persianToEnglishText(pref.getValue(AppConstants.TAX)?:"0.0")
        var tax = "0.0"
        var initialTotal= "0.0"
        var finalTotal= "0.0"

        if (products?.list?.isNotEmpty() == true)
        {
            for ( i in products.list!!){
                val netPriceProduct : String = NumberHelper.persianToEnglishText(i.difPrice?:"0.0")
                val count = NumberHelper.persianToEnglishText(i.count.toString()).toDouble()

                initialTotal = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(initialTotal).toDouble()
                        + (count * netPriceProduct.toDouble())).toString())

                if (i.taxAvailable == Taxable.YES.value.toString()){
                    val taxProductPrice = ((NumberHelper.persianToEnglishText(netPriceProduct).toDouble()
                            * taxSystem.toDouble()) / 100).toString()
                    tax = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(tax).toDouble()
                            + (count * NumberHelper.persianToEnglishText(taxProductPrice?:"0.0").toDouble())).toString())
                }
                finalTotal = NumberHelper.persianToEnglishText((initialTotal.toDouble() + tax.toDouble()).toString())
            }
        }
        products?.initialTotal = initialTotal.toDouble().toTwoDigits().toString()
        products?.finalTotal = finalTotal.toDouble().toTwoDigits().toString()
        products?.tax = tax.toDouble().toTwoDigits().toString()
        return products
    }
}