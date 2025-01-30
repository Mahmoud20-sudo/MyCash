package com.codeIn.myCash.features.stock.domain.receipt.repository

import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState

interface GetReceiptsRepository {

    suspend fun invoke(
        invoiceId : String?,
        clientId : String?,
        paymentStatus: String?,
        dateType: String?,
        sort: String?,
        dateFrom: String?,
        dateTo: String?,
        limit : String? ,
        date : String?):ReceiptState
}