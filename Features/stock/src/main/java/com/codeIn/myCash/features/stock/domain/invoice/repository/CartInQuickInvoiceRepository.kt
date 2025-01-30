package com.codeIn.myCash.features.stock.domain.invoice.repository

import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice

interface CartInQuickInvoiceRepository {
    suspend fun addInCart(productModel: ProductInQuickInvoice, productsInCart: ProductsInCartInQuickInvoice?): ProductsInCartInQuickInvoice?
    suspend fun deleteFromCart(productModel: ProductInQuickInvoice, productsInCart: ProductsInCartInQuickInvoice?): ProductsInCartInQuickInvoice?
    suspend fun updateInCart(productModel: ProductInQuickInvoice, productsInCart: ProductsInCartInQuickInvoice?): ProductsInCartInQuickInvoice?
}