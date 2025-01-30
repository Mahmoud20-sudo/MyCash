package com.codeIn.myCash.features.stock.domain.madaTransactions.repository

import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionState

interface MadaTransactionsRepository {

    suspend fun createTransaction(
        amount : String?,
        runRefund : String?,
        codeRefund : String?,
        dateRefund : String?,
        type : String?,
    ): TransactionState

    suspend fun getTransactions() : TransactionState


}