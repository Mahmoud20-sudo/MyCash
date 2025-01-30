package com.codeIn.myCash.features.sales.data.clients.remote.response

import com.google.gson.annotations.SerializedName

data class ClientsDTO(
    @SerializedName("data")
    val `data`: ClientsData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)