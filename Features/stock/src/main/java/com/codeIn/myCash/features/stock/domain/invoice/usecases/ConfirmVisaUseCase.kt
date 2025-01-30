package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import javax.inject.Inject

class ConfirmVisaUseCase @Inject constructor(private val invoiceRepository: InvoiceRepository){

    suspend operator fun invoke(
        invoiceId : String?,
        runRefundInvoice : String?,
        codeRefundInvoice : String?,
        dateRefundInvoice : String?,
    ): InvoiceState {
        return invoiceRepository.confirmVisa(
            invoiceId , runRefundInvoice , codeRefundInvoice , dateRefundInvoice
        )
    }
}