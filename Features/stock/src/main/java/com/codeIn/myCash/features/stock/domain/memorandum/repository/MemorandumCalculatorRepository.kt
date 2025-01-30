package com.codeIn.myCash.features.stock.domain.memorandum.repository

import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum

interface MemorandumCalculatorRepository {

    suspend fun makeCalculationOfInvoice(products: ProductInInvoiceMemorandum?): ProductInInvoiceMemorandum?

}