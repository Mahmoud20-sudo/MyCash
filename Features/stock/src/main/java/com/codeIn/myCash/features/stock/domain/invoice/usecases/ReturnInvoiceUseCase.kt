package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import javax.inject.Inject

class ReturnInvoiceUseCase @Inject constructor(private val invoiceRepository: InvoiceRepository){

    suspend operator fun invoke(
        cashPrice: String?,
        visaPrice: String?,
        paymentType: Int,
        products: ProductInReturnInvoice?,
        clientId : String?,
        invoiceId : String?,
        invoiceType : Int,
        codeRefundInvoice : String?,
        runRefundInvoice : String?,
        dateRefundInvoice : String?,
        mainTypeInvoice: Int
    ): InvoiceState {
        return invoiceRepository.refundInvoice(
          cashPrice, visaPrice, paymentType, products, clientId, invoiceId, invoiceType,
            codeRefundInvoice, runRefundInvoice, dateRefundInvoice , mainTypeInvoice
        )
    }
}