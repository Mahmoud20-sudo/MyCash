package com.codeIn.myCash.features.stock.data.invoice.remote.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class InvoiceState{

    data object Idle : InvoiceState()

    data object Loading : InvoiceState()

    data class SuccessShowInvoices(val data : InvoicesData?) : InvoiceState()

    data class SuccessShowSingleInvoice(val data : InvoiceModel?) : InvoiceState()

    data class ServerError(val error : ErrorEntity) : InvoiceState()

    data class StateError(val message : String?) : InvoiceState()
    data class Sucess(val message : String?) : InvoiceState()

    data class SuccessConfirmVisa(val data : InvoiceModel?) : InvoiceState()
    data class InputError(val inputError : InvalidInput) : InvoiceState()
    data object UnAuthorized: InvoiceState()
}