package com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt

import com.google.gson.annotations.SerializedName

data class DiscountCodeDTO(
    @SerializedName("data")
    val `data`: DiscountCodeData,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)
