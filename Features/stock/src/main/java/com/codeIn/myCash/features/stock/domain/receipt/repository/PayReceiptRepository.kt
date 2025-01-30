package com.codeIn.myCash.features.stock.domain.receipt.repository

import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import retrofit2.http.Field

interface PayReceiptRepository {

    suspend fun payReceipt(
        receiptId : String?,
        cashPrice : String?,
        visaPrice : String?,
        nextDate : String?,
        runRefundInvoice : String?,
        codeRefundInvoice : String?,
        dateRefundInvoice : String?,
        ):ReceiptState
}