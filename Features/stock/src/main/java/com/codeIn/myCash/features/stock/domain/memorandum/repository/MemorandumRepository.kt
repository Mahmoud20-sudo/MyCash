package com.codeIn.myCash.features.stock.domain.memorandum.repository

import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum

interface MemorandumRepository {

    suspend fun makeMemorandum(
        invoiceId: String? ,
        type : String?,
        cash : String?,
        visa : String?,
        products : ProductInInvoiceMemorandum? ,
        paymentType : String?
    ): MemorandumState

    suspend fun getMemorandums(
        invoiceId : String? ,
        type: String?,
        limit : String?
    ):MemorandumState

    suspend fun getSingleMemorandum(
        memorandumId : String?,
    ):MemorandumState

    suspend fun getProductRequest(
        products : ProductInInvoiceMemorandum?
    ): String?


    suspend fun validateItemInProducts(
        products : ProductInInvoiceMemorandum?
    ): MemorandumState
}