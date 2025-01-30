package com.codeIn.myCash.features.user.domain.accountSettings.repository

import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState

interface ValuesInvoiceSettingsRepository {

    suspend fun getValues() : InvoiceSettingsState

    suspend fun setValues(
        productDesc : String?,
        footerText : String?,
        client : String?,
        cashier : String?,
        type : String?,//1=>simple , 2=>tax
        myCashQr : String?,
        zatcaQr : String?,
        active : String?
    ) : InvoiceSettingsState


    suspend fun setMainValuesInvoiceSettings(
        commercialName : String? ,
        commercialNumber : String? ,
        taxRegistrationNumber : String? ,
        tax : String?
    ) : InvoiceSettingsState


    suspend fun startSaleInvoiceOrderNo(
        orderNo : String?
    ):InvoiceSettingsState

    suspend fun startPurchaseInvoiceOrderNo(
        orderNo : String?
    ):InvoiceSettingsState
}