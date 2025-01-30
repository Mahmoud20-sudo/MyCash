package com.codeIn.myCash.features.user.data.authentication.remote.response.user

import com.google.gson.annotations.SerializedName


data class UserDTO(
    @SerializedName("data")
    val `data`: User?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)