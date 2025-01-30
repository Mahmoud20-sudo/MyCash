package com.codeIn.myCash.features.stock.domain.receipt.usecases

import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.repository.GetReceiptsRepository
import javax.inject.Inject

class GetReceiptsUseCase @Inject constructor(private val getReceiptsRepository: GetReceiptsRepository){
    suspend operator fun invoke(
        invoiceId : String?= null ,
        clientId : String?= null ,
        paymentStatus: String?= null ,
        dateType: String? = null ,
        sort: String?= null ,
        dateFrom: String?= null ,
        dateTo: String?= null ,
        limit : String? = null ,
        date : String? = null
    ): ReceiptState {
        return getReceiptsRepository.invoke(invoiceId, clientId, paymentStatus, dateType, sort, dateFrom, dateTo, limit, date )
    }
}