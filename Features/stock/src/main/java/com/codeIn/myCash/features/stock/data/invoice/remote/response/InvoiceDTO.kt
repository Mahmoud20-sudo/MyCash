package com.codeIn.myCash.features.stock.data.invoice.remote.response

import com.google.gson.annotations.SerializedName

data class InvoiceDTO(
    @SerializedName("data")
    val invoiceModel: InvoiceModel,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)