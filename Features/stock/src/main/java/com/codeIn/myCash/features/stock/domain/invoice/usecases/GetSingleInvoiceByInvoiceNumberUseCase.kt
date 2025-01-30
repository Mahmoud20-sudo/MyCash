package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceRepository
import javax.inject.Inject

class GetSingleInvoiceByInvoiceNumberUseCase @Inject constructor(private val invoiceRepository: InvoiceRepository){
    suspend operator fun invoke(
        invoiceNumber : String?
    ): InvoiceState {
        return invoiceRepository.getSingleInvoiceWithNumber(invoiceNumber)
    }
}