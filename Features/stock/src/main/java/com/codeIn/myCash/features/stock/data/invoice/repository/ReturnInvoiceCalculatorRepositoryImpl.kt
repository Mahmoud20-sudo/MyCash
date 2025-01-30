package com.codeIn.myCash.features.stock.data.invoice.repository

import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Discount
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.Taxable
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.toTwoDigits
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.ReturnInvoiceCalculatorRepository
import javax.inject.Inject
import kotlin.math.abs

class ReturnInvoiceCalculatorRepositoryImpl @Inject constructor(
    private val pref : SharedPrefsModule
) : ReturnInvoiceCalculatorRepository {
    override suspend fun makeCalculationOfInvoice(products: ProductInReturnInvoice?): ProductInReturnInvoice? {
        val taxSystem = NumberHelper.persianToEnglishText(pref.getValue(AppConstants.TAX)?:"0.0")
        var tax = "0.0"
        var discount = "0.0"
        var initialTotal= "0.0"
        var totalAfterDiscount = "0.0"
        var totalAfterNotification = "0.0"
        var finalTotal= "0.0"
        var totalNotification :Double = 0.0

        if (products?.list?.isNotEmpty() == true)
        {
            for ( i in products.list!!){
                var productDiscount : String = "0.0"
                var invoiceDiscount : String = "0.0"
                var netPriceProduct : String = NumberHelper.persianToEnglishText(i.unitPrice?:"0.0")
                var productPriceAfterDiscount : String= netPriceProduct
                val count = NumberHelper.persianToEnglishText(i.count.toString()).toDouble()

                initialTotal = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(initialTotal).toDouble()
                + (count * netPriceProduct.toDouble())).toString())

                if(   abs(i.notificationPrice?.toFloat() ?: 0f) > 0f   ){
                    val realPrice = (i.unitPrice?.toFloat() ?: 0f) + (i.notificationPrice?.toFloat() ?: 0f)
                    netPriceProduct = NumberHelper.persianToEnglishText(realPrice.toString())
                }

                productDiscount = NumberHelper.persianToEnglishText(i.unitDiscountPrice?:"0.0")
                discount = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(discount).toDouble()
                        + (count * productDiscount.toDouble())).toString())
                productPriceAfterDiscount = NumberHelper.persianToEnglishText((netPriceProduct.toDouble() - productDiscount.toDouble()).toString()?:"0.0")

                if (i.product?.taxAvailable == Taxable.YES.value.toString()){
                    val taxProductPrice = ((NumberHelper.persianToEnglishText(productPriceAfterDiscount).toDouble()
                        * taxSystem.toDouble()) / 100).toString()
                    tax = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(tax).toDouble()
                            + (count * NumberHelper.persianToEnglishText(taxProductPrice?:"0.0").toDouble())).toString())
                }

                totalAfterDiscount = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(totalAfterDiscount).toDouble()
                        + (productPriceAfterDiscount.toDouble() * count)).toString())

                finalTotal = NumberHelper.persianToEnglishText((totalAfterDiscount.toDouble() + tax.toDouble()).toString())

                totalNotification += i.totalAfterNotification?.toDouble() ?: 0.0
            }
        }
        products?.initialTotal = initialTotal.toDouble().toTwoDigits().toString()
        products?.discount = discount.toDouble().toTwoDigits().toString()
        products?.finalTotal = finalTotal.toDouble().toTwoDigits().toString()
        products?.totalAfterDiscount = totalAfterDiscount.toDouble().toTwoDigits().toString()
        products?.totalAfterNotification = NumberHelper.persianToEnglishText(totalNotification.toString())

        products?.tax = tax.toDouble().toTwoDigits().toString()
        return products
    }
}