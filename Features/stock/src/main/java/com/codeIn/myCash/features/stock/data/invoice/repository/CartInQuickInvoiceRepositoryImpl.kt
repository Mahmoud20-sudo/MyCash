package com.codeIn.myCash.features.stock.data.invoice.repository

import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartInQuickInvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.usecases.CalculateQuickInvoiceUseCase
import javax.inject.Inject

class CartInQuickInvoiceRepositoryImpl @Inject constructor(
    private val useCase : CalculateQuickInvoiceUseCase
) : CartInQuickInvoiceRepository {

    override suspend fun addInCart(
        productModel: ProductInQuickInvoice,
        productsInCart: ProductsInCartInQuickInvoice?
    ): ProductsInCartInQuickInvoice? {
        productsInCart?.list?.add(productModel)
        productsInCart?.count = productsInCart?.count!! + 1
        useCase.invoke(productsInCart).let {
            return it
        }
    }

    override suspend fun deleteFromCart(
        productModel: ProductInQuickInvoice,
        productsInCart: ProductsInCartInQuickInvoice?
    ): ProductsInCartInQuickInvoice? {
        productsInCart?.list?.remove(productModel)
        productsInCart?.count = productsInCart?.count!! - 1
        useCase.invoke(productsInCart).let {
            return it
        }
    }

    override suspend fun updateInCart(
        productModel: ProductInQuickInvoice,
        productsInCart: ProductsInCartInQuickInvoice?
    ): ProductsInCartInQuickInvoice? {
        val index = productsInCart?.list?.indexOfFirst { it.name == productModel.name}
        if (index != -1 )
            productsInCart?.list?.set(index!!, productModel)
        useCase.invoke(productsInCart).let {
            return it
        }
    }
}