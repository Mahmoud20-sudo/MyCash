package com.codeIn.myCash.features.stock.data.invoice.remote.response

import com.google.gson.annotations.SerializedName

data class InvoicesDTO(
    @SerializedName("data")
    val `data`: InvoicesData,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)