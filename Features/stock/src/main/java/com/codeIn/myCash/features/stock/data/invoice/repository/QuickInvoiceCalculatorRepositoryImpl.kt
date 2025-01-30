package com.codeIn.myCash.features.stock.data.invoice.repository

import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.TaxType
import com.codeIn.common.data.Taxable
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.toTwoDigits
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.QuickInvoiceCalculatorRepository
import javax.inject.Inject

class QuickInvoiceCalculatorRepositoryImpl @Inject constructor(
    private val pref : SharedPrefsModule
) : QuickInvoiceCalculatorRepository {
    override suspend fun makeCalculationOfInvoice(productsInCart: ProductsInCartInQuickInvoice?): ProductsInCartInQuickInvoice? {
        val taxSystem = NumberHelper.persianToEnglishText(pref.getValue(AppConstants.TAX)?:"0.0")
        var tax = "0.0"
        var initialTotal= "0.0"
        var finalTotal= "0.0"

        if (productsInCart?.list?.isNotEmpty() == true)
        {
            for ( i in productsInCart.list!!){
                var netPriceProduct : String = NumberHelper.persianToEnglishText(i.price?:"0.0")
                val count = NumberHelper.persianToEnglishText(i.quantity.toString()).toInt()

                if (i.taxAvailable == Taxable.YES.value.toString()){
                    val taxProductPrice = if (i.taxType == TaxType.Excludes.value.toString()){
                        ((NumberHelper.persianToEnglishText(netPriceProduct).toDouble()
                                * taxSystem.toDouble()) / 100).toString()
                    }
                    else {
                        val taxSystemValue = taxSystem.toDouble().toInt()
                        val taxValue = NumberHelper.persianToEnglishText("1.$taxSystemValue").toDouble()
                        netPriceProduct = (NumberHelper.persianToEnglishText(netPriceProduct).toDouble() / taxValue).toString()
                        NumberHelper.persianToEnglishText(((i.price?:"0.0").toDouble() - netPriceProduct.toDouble()).toString())
                    }
                    tax = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(tax).toDouble()
                            + (count * NumberHelper.persianToEnglishText(taxProductPrice?:"0.0").toDouble())).toString())
                }

                initialTotal = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(initialTotal).toDouble()
                        + (count * netPriceProduct.toDouble())).toString())

                finalTotal = NumberHelper.persianToEnglishText((initialTotal.toDouble() + tax.toDouble()).toString())
            }
        }
        productsInCart?.initialTotal = initialTotal.toDouble().toTwoDigits().toString()
        productsInCart?.finalTotal = finalTotal.toDouble().toTwoDigits().toString()
        productsInCart?.tax = tax.toDouble().toTwoDigits().toString()
        return productsInCart
    }
}