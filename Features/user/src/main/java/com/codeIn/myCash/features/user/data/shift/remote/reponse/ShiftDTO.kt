package com.codeIn.myCash.features.user.data.shift.remote.reponse

import com.google.gson.annotations.SerializedName

data class ShiftDTO(
    @SerializedName("data")
    val `data`: ShiftData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)