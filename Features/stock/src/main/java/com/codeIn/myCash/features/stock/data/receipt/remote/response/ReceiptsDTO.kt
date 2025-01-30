package com.codeIn.myCash.features.stock.data.receipt.remote.response

import com.google.gson.annotations.SerializedName

data class ReceiptsDTO(
    @SerializedName("data")
    val `data`: ReceiptsData,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)