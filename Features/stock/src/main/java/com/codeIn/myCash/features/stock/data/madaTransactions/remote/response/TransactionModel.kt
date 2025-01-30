package com.codeIn.myCash.features.stock.data.madaTransactions.remote.response

data class TransactionModel(
    val amount: String?,
    val codeRefund: String?,
    val dateRefund: String?,
    val id: Int,
    val runRefund: String?,
    val type: String?
)