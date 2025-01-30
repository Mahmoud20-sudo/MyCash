package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.repository.MemorandumCalculatorRepository
import javax.inject.Inject

class CalculateMemorandumInvoiceUseCase @Inject constructor(
    private val memorandumCalculatorRepository: MemorandumCalculatorRepository
) {
    suspend operator fun invoke(products: ProductInInvoiceMemorandum?): ProductInInvoiceMemorandum?{
        return memorandumCalculatorRepository.makeCalculationOfInvoice(products)
    }
}