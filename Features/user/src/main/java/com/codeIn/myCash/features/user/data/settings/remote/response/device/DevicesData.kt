package com.codeIn.myCash.features.user.data.settings.remote.response.device

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class DevicesData(
    @SerializedName("data")
    val `data`: List<DeviceModel>?,
    @SerializedName("pagination")
    val pagination: Pagination
)
