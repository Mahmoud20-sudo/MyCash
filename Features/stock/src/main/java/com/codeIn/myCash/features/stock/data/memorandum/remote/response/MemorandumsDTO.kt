package com.codeIn.myCash.features.stock.data.memorandum.remote.response

import com.google.gson.annotations.SerializedName

data class MemorandumsDTO(
    @SerializedName("data")
    val memorandumsData: MemorandumsData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)