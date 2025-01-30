package com.codeIn.myCash.features.stock.data.receipt.remote.response

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class ReceiptsData(
    @SerializedName("data")
    val `data`: List<ReceiptModel>?,
    @SerializedName("pagination")
    val pagination: Pagination
)