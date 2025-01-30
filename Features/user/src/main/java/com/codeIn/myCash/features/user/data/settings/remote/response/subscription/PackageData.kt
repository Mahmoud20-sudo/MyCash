package com.codeIn.myCash.features.user.data.settings.remote.response.subscription

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class PackageData(
    @SerializedName("data")
    val `data`: List<Package>?,
    @SerializedName("pagination")
    val pagination: Pagination
)
