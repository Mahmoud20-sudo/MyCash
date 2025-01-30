package com.codeIn.myCash.features.stock.data.memorandum.remote.response

import com.google.gson.annotations.SerializedName

data class MemorandumDTO(
    @SerializedName("data")
    val `data`: MemorandumModel?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)