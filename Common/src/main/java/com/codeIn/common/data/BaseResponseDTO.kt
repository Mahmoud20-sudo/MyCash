package com.codeIn.common.data

import com.google.gson.annotations.SerializedName
data class BaseResponseDTO(
    @SerializedName("data")
    val `data`: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)