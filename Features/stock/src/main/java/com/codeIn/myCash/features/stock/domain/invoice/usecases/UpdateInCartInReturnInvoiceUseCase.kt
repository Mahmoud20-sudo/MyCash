package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartInReturnInvoiceRepository
import javax.inject.Inject

class UpdateInCartInReturnInvoiceUseCase  @Inject constructor(private val cartInReturnInvoiceRepository: CartInReturnInvoiceRepository) {
    suspend operator fun invoke(productModel: ProductInInvoiceModel?, productInReturnInvoice: ProductInReturnInvoice?) : ProductInReturnInvoice? {
        return cartInReturnInvoiceRepository.updateInInvoice(productModel , productInReturnInvoice)
    }
}