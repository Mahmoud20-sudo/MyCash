package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.ReturnInvoiceCalculatorRepository
import javax.inject.Inject

class CalculateReturnInvoiceUseCase @Inject constructor(
    private val returnInvoiceCalculatorRepository: ReturnInvoiceCalculatorRepository
) {
    suspend operator fun invoke(products: ProductInReturnInvoice?): ProductInReturnInvoice?{
        return returnInvoiceCalculatorRepository.makeCalculationOfInvoice(products)
    }
}