package com.codeIn.myCash.features.stock.domain.receipt.usecases

import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.repository.PayReceiptRepository
import javax.inject.Inject

class PayReceiptUseCase  @Inject constructor(private val payReceiptRepository: PayReceiptRepository){

    suspend operator fun invoke( receiptId : String?,
                                 cashPrice : String?,
                                 visaPrice : String?,
                                 nextDate : String?,
                                 runRefundInvoice : String?,
                                 codeRefundInvoice : String?,
                                 dateRefundInvoice : String?,
                                 ) : ReceiptState{

        return payReceiptRepository.payReceipt(receiptId, cashPrice, visaPrice , nextDate ,
            runRefundInvoice, codeRefundInvoice, dateRefundInvoice)

    }
}