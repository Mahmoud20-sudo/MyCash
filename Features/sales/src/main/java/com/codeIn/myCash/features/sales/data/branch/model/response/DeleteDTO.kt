package com.codeIn.myCash.features.sales.data.branch.model.response


import com.google.gson.annotations.SerializedName

data class DeleteDTO(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: Any?
)