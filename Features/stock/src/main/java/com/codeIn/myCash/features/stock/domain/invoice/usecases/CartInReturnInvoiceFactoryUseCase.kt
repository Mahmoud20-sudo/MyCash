package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.common.data.Cart
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import javax.inject.Inject

class CartInReturnInvoiceFactoryUseCase @Inject constructor(
    private val deleteFromCartInReturnInvoiceUseCase: DeleteFromCartInReturnInvoiceUseCase,
    private val updateInCartInReturnInvoiceUseCase: UpdateInCartInReturnInvoiceUseCase,
    private val calculateReturnInvoiceUseCase: CalculateReturnInvoiceUseCase
){
    suspend operator fun invoke(productModel: ProductInInvoiceModel?, products: ProductInReturnInvoice?, cart: Cart) : ProductInReturnInvoice? {
        return when (cart) {
            Cart.ADD -> {
                updateInCartInReturnInvoiceUseCase.invoke(productModel , products)
            }
            Cart.DELETE -> {
                deleteFromCartInReturnInvoiceUseCase.invoke(productModel, products)
            }
            Cart.INITIAL ->{
                calculateReturnInvoiceUseCase.invoke(products)
            }
            else -> null
        }
    }
}