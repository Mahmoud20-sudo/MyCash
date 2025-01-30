package com.codeIn.myCash.features.user.data.settings.remote.response.device

import com.google.gson.annotations.SerializedName

data class DevicesDTO(
    @SerializedName("data")
    val `data`: DevicesData,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)
