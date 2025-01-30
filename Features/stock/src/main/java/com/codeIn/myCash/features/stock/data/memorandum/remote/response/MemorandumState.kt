package com.codeIn.myCash.features.stock.data.memorandum.remote.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState

sealed class MemorandumState{

    data object Idle : MemorandumState()

    data object Loading : MemorandumState()

    data class SuccessShowMemorandums(val data : MemorandumsData?) : MemorandumState()

    data class SuccessShowSingleMemorandum(val data : MemorandumModel?) : MemorandumState()

    data object SuccessValidationInput : MemorandumState()
    data class ServerError(val error : ErrorEntity) : MemorandumState()

    data class StateError(val message : String?) : MemorandumState()

    data class InputError(val inputError : InvalidInput) : MemorandumState()
    data object UnAuthorized : MemorandumState()
}