package com.codeIn.myCash.features.stock.data.invoice.repository

import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Discount
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.Taxable
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.toTwoDigits
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceCalculatorRepository
import javax.inject.Inject

class InvoiceCalculatorRepositoryImpl @Inject constructor(
    private val pref : SharedPrefsModule
) : InvoiceCalculatorRepository {
    override suspend fun makeCalculationOfInvoice(productsInCart: ProductsInCart?): ProductsInCart? {
        val taxSystem = NumberHelper.persianToEnglishText(pref.getValue(AppConstants.TAX)?:"0.0")
        var tax = "0.0"
        var discount = "0.0"
        var initialTotal= "0.0"
        var totalAfterDiscount = "0.0"
        var finalTotal= "0.0"

        if (productsInCart?.list?.isNotEmpty() == true)
        {
            for ( i in productsInCart.list!!){
                var productDiscount : String = "0.0"
                var invoiceDiscount : String = "0.0"
                val netPriceProduct : String = NumberHelper.persianToEnglishText(i.productPrice?:"0.0")
                var productPriceAfterDiscount : String= netPriceProduct
                val count = NumberHelper.persianToEnglishText(i.count.toString()).toInt()

                initialTotal = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(initialTotal).toDouble()
                + (count * netPriceProduct.toDouble())).toString())

                if (i.hasDiscount == "1"){
                    productDiscount = NumberHelper.persianToEnglishText(i.discountPrice?:"0.0")
                    discount = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(discount).toDouble()
                            + (count * productDiscount.toDouble())).toString())
                    productPriceAfterDiscount =
                        NumberHelper.persianToEnglishText(i.productPriceAfterDiscount?:"0.0")
                }

                if (i.discountInInvoiceModel?.discountType == Discount.Value || i.discountInInvoiceModel?.discountType == Discount.Percentage){
                    if(i.discountInInvoiceModel?.discountType == Discount.Value){
                        invoiceDiscount = NumberHelper.persianToEnglishText(i.discountInInvoiceModel?.discountValue?:"0.0")
                        discount = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(discount).toDouble()
                                + (count * invoiceDiscount.toDouble())).toString())
                    }
                    else if(i.discountInInvoiceModel?.discountType == Discount.Percentage){
                        invoiceDiscount = NumberHelper.persianToEnglishText(((NumberHelper.persianToEnglishText(
                            i.discountInInvoiceModel?.discountValue ?:"0.0").toDouble() * (productPriceAfterDiscount?:"0.0").toDouble())/100).toString())

                        discount = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(discount).toDouble()
                                + (count * invoiceDiscount.toDouble())).toString())
                    }

                    productPriceAfterDiscount = NumberHelper.persianToEnglishText((
                            NumberHelper.persianToEnglishText(productPriceAfterDiscount?:"0.0").toDouble()
                                    - invoiceDiscount.toDouble()).toString())
                }

                if (i.taxAvailable == Taxable.YES.value.toString()){
                    val taxProductPrice = ((NumberHelper.persianToEnglishText(productPriceAfterDiscount).toDouble()
                        * taxSystem.toDouble()) / 100).toString()
                    tax = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(tax).toDouble()
                            + (count * NumberHelper.persianToEnglishText(taxProductPrice?:"0.0").toDouble())).toString())
                }

                totalAfterDiscount = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(totalAfterDiscount).toDouble()
                        + (productPriceAfterDiscount.toDouble() * count)).toString())

                finalTotal = NumberHelper.persianToEnglishText((totalAfterDiscount.toDouble() + tax.toDouble()).toString())
            }
        }
        productsInCart?.initialTotal = initialTotal.toDouble().toTwoDigits().toString()
        productsInCart?.discount = discount.toDouble().toTwoDigits().toString()
        productsInCart?.finalTotal = finalTotal.toDouble().toTwoDigits().toString()
        productsInCart?.totalAfterDiscount = totalAfterDiscount.toDouble().toTwoDigits().toString()
        productsInCart?.tax = tax.toDouble().toTwoDigits().toString()
        return productsInCart
    }

    override suspend fun makeCalculationOfPurchaseInvoice(productsInCart: ProductsInCart?): ProductsInCart? {
        var tax = "0.0"
        val discount = "0.0"
        var initialTotal= "0.0"
        var totalAfterDiscount = "0.0"
        var finalTotal= "0.0"

        if (productsInCart?.list?.isNotEmpty() == true)
        {
            for ( i in productsInCart.list!!){
                val netPriceProduct : String = NumberHelper.persianToEnglishText(i.productBuyPrice?:"0.0")
                val productPriceAfterDiscount : String= netPriceProduct
                val count = NumberHelper.persianToEnglishText(i.count.toString()).toInt()

                initialTotal = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(initialTotal).toDouble()
                        + (count * netPriceProduct.toDouble())).toString())

                if (i.buyTaxAvailable == Taxable.YES.value.toString()){
                    tax = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(tax).toDouble()
                            + (count * NumberHelper.persianToEnglishText(i.buyTaxPrice?:"0.0").toDouble())).toString())
                }

                totalAfterDiscount = NumberHelper.persianToEnglishText((NumberHelper.persianToEnglishText(totalAfterDiscount).toDouble()
                        + (productPriceAfterDiscount.toDouble() * count)).toString())

                finalTotal = NumberHelper.persianToEnglishText((totalAfterDiscount.toDouble() + tax.toDouble()).toString())
            }
        }
        productsInCart?.initialTotal = initialTotal.toDouble().toTwoDigits().toString()
        productsInCart?.discount = discount.toDouble().toTwoDigits().toString()
        productsInCart?.finalTotal = finalTotal.toDouble().toTwoDigits().toString()
        productsInCart?.totalAfterDiscount = totalAfterDiscount.toDouble().toTwoDigits().toString()
        productsInCart?.tax = tax.toDouble().toTwoDigits().toString()
        return productsInCart
    }
}