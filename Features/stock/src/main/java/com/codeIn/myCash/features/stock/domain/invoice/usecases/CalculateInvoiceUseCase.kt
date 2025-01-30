package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceCalculatorRepository
import javax.inject.Inject

class CalculateInvoiceUseCase @Inject constructor(
    private val invoiceCalculatorRepository: InvoiceCalculatorRepository
) {
    suspend operator fun invoke(productsInCart: ProductsInCart?): ProductsInCart?{
        return invoiceCalculatorRepository.makeCalculationOfInvoice(productsInCart)
    }
}