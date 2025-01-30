package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.QuickInvoiceCalculatorRepository
import javax.inject.Inject

class CalculateQuickInvoiceUseCase @Inject constructor(
    private val quickInvoiceCalculatorRepository: QuickInvoiceCalculatorRepository
) {
    suspend operator fun invoke(products: ProductsInCartInQuickInvoice?): ProductsInCartInQuickInvoice?{
        return quickInvoiceCalculatorRepository.makeCalculationOfInvoice(products)
    }
}