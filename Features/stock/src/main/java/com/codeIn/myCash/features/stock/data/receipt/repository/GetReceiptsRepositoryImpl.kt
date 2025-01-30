package com.codeIn.myCash.features.stock.data.receipt.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.codeIn.myCash.features.stock.data.receipt.remote.Receipt
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.repository.GetReceiptsRepository
import javax.inject.Inject

class GetReceiptsRepositoryImpl  @Inject constructor(
    private val receipt: Receipt ,
    private val prefs: SharedPrefsModule ,
    private val errorHandler: ErrorHandlerImpl
): GetReceiptsRepository{
    override suspend fun invoke(
        invoiceId: String?,
        clientId: String?,
        paymentStatus: String?,
        dateType: String?,
        sort: String?,
        dateFrom: String?,
        dateTo: String?,
        limit: String? ,
        date : String?
    ): ReceiptState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            receipt.getReceipts(
                lang= lang ,
                authorization = token ,
                clientId = clientId ,
                invoiceId = invoiceId ,
                paymentStatus = paymentStatus ,
                dateType = dateType ,
                dateTo = dateTo ,
                dateFrom = dateFrom ,
                limit = limit ,
                sort = sort ,
                date = date
            ).let { response ->
                Log.d("TAG" , "Receipts ,, $response , $invoiceId")
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        ReceiptState.SuccessShowReceipts(response.body()?.data)
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