package com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings

import com.google.gson.annotations.SerializedName

data class InvoicesSettingsDTO(
    @SerializedName("data")
    val `data`: InvoiceSettingsData,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)




