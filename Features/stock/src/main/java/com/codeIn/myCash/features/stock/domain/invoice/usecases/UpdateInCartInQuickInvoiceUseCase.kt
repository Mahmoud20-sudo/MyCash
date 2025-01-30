package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartInQuickInvoiceRepository
import javax.inject.Inject

class UpdateInCartInQuickInvoiceUseCase @Inject constructor(private val cartRepository : CartInQuickInvoiceRepository) {
    suspend operator fun invoke(productModel: ProductInQuickInvoice, productsInCart: ProductsInCartInQuickInvoice?) : ProductsInCartInQuickInvoice?{
        return cartRepository.updateInCart(productModel , productsInCart)
    }
}