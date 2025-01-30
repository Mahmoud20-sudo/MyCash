package com.codeIn.myCash.features.stock.domain.invoice.repository

import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice

interface CartInReturnInvoiceRepository {
    suspend fun deleteFromInvoice(productModel: ProductInInvoiceModel? , productsInCart: ProductInReturnInvoice?): ProductInReturnInvoice?
    suspend fun updateInInvoice(productModel: ProductInInvoiceModel? , productsInCart: ProductInReturnInvoice?): ProductInReturnInvoice?
}