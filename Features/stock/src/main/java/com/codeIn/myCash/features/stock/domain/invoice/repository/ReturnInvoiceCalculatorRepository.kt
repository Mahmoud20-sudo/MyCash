package com.codeIn.myCash.features.stock.domain.invoice.repository

import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice

interface ReturnInvoiceCalculatorRepository {

    suspend fun makeCalculationOfInvoice(products: ProductInReturnInvoice?): ProductInReturnInvoice?
}