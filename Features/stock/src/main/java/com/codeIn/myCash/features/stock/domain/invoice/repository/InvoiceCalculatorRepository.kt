package com.codeIn.myCash.features.stock.domain.invoice.repository

import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart

interface InvoiceCalculatorRepository {
    suspend fun makeCalculationOfInvoice(productsInCart: ProductsInCart?): ProductsInCart?
    suspend fun makeCalculationOfPurchaseInvoice(productsInCart: ProductsInCart?): ProductsInCart?
}