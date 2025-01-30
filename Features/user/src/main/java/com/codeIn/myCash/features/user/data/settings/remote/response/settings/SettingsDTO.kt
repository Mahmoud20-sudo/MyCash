package com.codeIn.myCash.features.user.data.settings.remote.response.settings

import com.google.gson.annotations.SerializedName

data class SettingsDTO(
    @SerializedName("data")
    val `data`: SettingsData,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)

