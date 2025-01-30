package com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class InvoiceSettingsState{
    data object Idle : InvoiceSettingsState()

    data object Loading : InvoiceSettingsState()

    data class InvoiceSettingsSuccess(val data : InvoiceSettingsData?) : InvoiceSettingsState()

    data class InvoiceTypeSuccess(val message: String?) : InvoiceSettingsState()
    data class OrderNoInvoiceSuccess(val message: String?) : InvoiceSettingsState()

    data class ServerError(val error : ErrorEntity) : InvoiceSettingsState()

    data class StateError(val message : String?) : InvoiceSettingsState()

    data class InputError(val inputError : InvalidInput) : InvoiceSettingsState()
    data object UnAuthorized: InvoiceSettingsState()
}