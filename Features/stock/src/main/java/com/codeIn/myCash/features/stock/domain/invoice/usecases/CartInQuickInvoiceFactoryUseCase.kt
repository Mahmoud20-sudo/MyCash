package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.common.data.Cart
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import javax.inject.Inject

class CartInQuickInvoiceFactoryUseCase @Inject constructor(
    private val addToCartInQuickInvoiceUseCase: AddToCartInQuickInvoiceUseCase,
    private val deleteFromCartInQuickInvoiceUseCase: DeleteFromCartInQuickInvoiceUseCase,
    private val updateInCartInQuickInvoiceUseCase: UpdateInCartInQuickInvoiceUseCase
){
    suspend operator fun invoke(productModel: ProductInQuickInvoice,
                                productsInCart: ProductsInCartInQuickInvoice?, cart: Cart) : ProductsInCartInQuickInvoice? {
        return when (cart) {
            Cart.UPDATE -> {
                updateInCartInQuickInvoiceUseCase.invoke(productModel , productsInCart)
            }
            Cart.ADD -> {
                addToCartInQuickInvoiceUseCase.invoke(productModel , productsInCart)
            }
            Cart.DELETE -> {
                deleteFromCartInQuickInvoiceUseCase.invoke(productModel, productsInCart)
            }
            else -> null
        }
    }
}