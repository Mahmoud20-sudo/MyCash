package com.codeIn.myCash.features.stock.data.madaTransactions.remote.response

data class TransactionsData(
    val `data`: List<TransactionModel>,
    val pagination: Pagination
)