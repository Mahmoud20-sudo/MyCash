package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceRepository
import javax.inject.Inject

class GetInvoicesUseCase @Inject constructor(private val invoiceRepository: InvoiceRepository){
    suspend operator fun invoke(
        type : String? ,
        isReturn : String? ,
        clientId : String? ,
        invoiceType : String? ,
        saleOrBuy : String?,
        paymentStatus : String? ,
        limit : String? ,
        date : String?
    ): InvoiceState {
        return invoiceRepository.getInvoices(type, isReturn, clientId, invoiceType,saleOrBuy, paymentStatus , limit , date )
    }
}