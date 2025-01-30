package com.codeIn.myCash.features.user.data.settings.remote.response.subscription

import com.google.gson.annotations.SerializedName

data class PackagesDTO(
    @SerializedName("data")
    val `data`: PackageData,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)


