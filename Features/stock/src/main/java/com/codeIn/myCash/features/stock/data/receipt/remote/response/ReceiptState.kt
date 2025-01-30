package com.codeIn.myCash.features.stock.data.receipt.remote.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class ReceiptState{

    data object Idle : ReceiptState()

    data object Loading : ReceiptState()

    data class SuccessShowReceipts(val data : ReceiptsData?) : ReceiptState()

    data class SuccessShowSingleReceipt(val data : ReceiptModel?) : ReceiptState()

    data class SuccessPayReceipt(val message: String?) : ReceiptState()

    data class ServerError(val error : ErrorEntity) : ReceiptState()

    data class StateError(val message : String?) : ReceiptState()
    data object UnAuthorized: ReceiptState()

    data class InputError(val inputError : InvalidInput) : ReceiptState()
}