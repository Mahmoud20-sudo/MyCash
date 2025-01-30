package com.codeIn.myCash.features.stock.data.receipt.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.stock.data.receipt.remote.Receipt
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.repository.PayReceiptRepository
import retrofit2.http.Field
import javax.inject.Inject

class PayReceiptRepositoryImpl @Inject constructor(
    private val receipt: Receipt,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
): PayReceiptRepository{
    override suspend fun payReceipt(
        receiptId: String?,
        cashPrice: String?,
        visaPrice: String?,
        nextDate: String?,
        runRefundInvoice : String?,
        codeRefundInvoice : String?,
        dateRefundInvoice : String?,
    ): ReceiptState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            receipt.payReceipt(
                lang=lang ,
                authorization = token ,
                receiptId= receiptId ,
                cashPrice= cashPrice ,
                visaPrice = visaPrice,
                nextDate = nextDate,
                runRefundInvoice,
                codeRefundInvoice,
                dateRefundInvoice
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        ReceiptState.SuccessPayReceipt(response.body()?.message)
                    } else {
                        ReceiptState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    ReceiptState.UnAuthorized
                }  else {
                    ReceiptState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            ReceiptState.ServerError(errorHandler.getError(throwable))
        }
    }
}