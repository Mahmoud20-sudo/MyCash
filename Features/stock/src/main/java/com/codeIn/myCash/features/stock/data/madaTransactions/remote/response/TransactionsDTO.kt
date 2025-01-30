package com.codeIn.myCash.features.stock.data.madaTransactions.remote.response

data class TransactionsDTO(
    val `data`: TransactionsData,
    val message: Any,
    val status: Int
)