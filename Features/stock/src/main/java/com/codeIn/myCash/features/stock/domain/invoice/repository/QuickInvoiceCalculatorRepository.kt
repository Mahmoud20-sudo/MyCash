package com.codeIn.myCash.features.stock.domain.invoice.repository

import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice

interface QuickInvoiceCalculatorRepository {

    suspend fun makeCalculationOfInvoice(productsInCart: ProductsInCartInQuickInvoice?): ProductsInCartInQuickInvoice?
}