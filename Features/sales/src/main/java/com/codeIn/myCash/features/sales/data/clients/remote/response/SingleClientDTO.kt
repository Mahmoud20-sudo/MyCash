package com.codeIn.myCash.features.sales.data.clients.remote.response

import com.google.gson.annotations.SerializedName

data class SingleClientDTO(
    @SerializedName("data")
    val `data`: ClientModel?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)