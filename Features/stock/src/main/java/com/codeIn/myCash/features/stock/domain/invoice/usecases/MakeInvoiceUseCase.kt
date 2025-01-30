package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import javax.inject.Inject

class MakeInvoiceUseCase @Inject constructor(private val invoiceRepository: InvoiceRepository){

    suspend operator fun invoke(
        cashPrice : String?,
        visaPrice : String?,
        paymentType : Int,
        products : ProductsInCart?,
        nextData : String?,
    ): InvoiceState {
        return invoiceRepository.makeInvoice(
           cashPrice, visaPrice, paymentType, products, nextData,
        )
    }
}