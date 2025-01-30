package com.codeIn.myCash.features.sales.data.clients.remote.response

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class ClientsData(
    @SerializedName("data")
    val `data`: List<ClientModel>?,
    @SerializedName("pagination")
    val pagination: Pagination
)
