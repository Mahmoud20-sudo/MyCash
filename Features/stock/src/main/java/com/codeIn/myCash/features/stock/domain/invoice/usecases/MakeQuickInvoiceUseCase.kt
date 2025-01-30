package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import javax.inject.Inject

class MakeQuickInvoiceUseCase @Inject constructor(private val invoiceRepository: InvoiceRepository){

    suspend operator fun invoke(
        cashPrice : String?,
        visaPrice : String?,
        paymentType : Int,
        products : ProductsInCartInQuickInvoice?,
        nextData : String?,
        product : ProductInQuickInvoice?,
        runRefundInvoice : String?,
        codeRefundInvoice : String?,
        dateRefundInvoice : String?,
    ): InvoiceState {
        return invoiceRepository.makeQuickInvoice(
           cashPrice, visaPrice, paymentType, products, nextData, product , runRefundInvoice , codeRefundInvoice , dateRefundInvoice
        )
    }
}