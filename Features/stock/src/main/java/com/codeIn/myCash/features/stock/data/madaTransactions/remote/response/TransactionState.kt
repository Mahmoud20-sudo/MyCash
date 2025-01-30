package com.codeIn.myCash.features.stock.data.madaTransactions.remote.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class TransactionState{

    data object Idle : TransactionState()

    data object Loading : TransactionState()

    data class SuccessShowTransactions(val data : TransactionsData?) : TransactionState()

    data class SuccessShowSingleTransaction(val data : TransactionModel?) : TransactionState()

    data class ServerError(val error : ErrorEntity) : TransactionState()

    data class StateError(val message : String?) : TransactionState()

    data class Sucess(val message : String?) : TransactionState()

    data class InputError(val inputError : InvalidInput) : TransactionState()
    data object UnAuthorized: TransactionState()
}