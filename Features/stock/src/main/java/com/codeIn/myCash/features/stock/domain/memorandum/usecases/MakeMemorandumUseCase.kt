package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.repository.MemorandumRepository
import javax.inject.Inject

class MakeMemorandumUseCase @Inject constructor(private val repository : MemorandumRepository){
    suspend operator fun invoke(
        invoiceId: String? ,
        type : String?,
        cash : String?,
        visa : String?,
        products : ProductInInvoiceMemorandum? ,
        paymentType : String?
    ): MemorandumState {
        return repository.makeMemorandum(invoiceId, type, cash, visa, products , paymentType)
    }
}