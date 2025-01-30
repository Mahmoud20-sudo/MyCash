package com.codeIn.myCash.features.user.data.users.model.response


import com.google.gson.annotations.SerializedName

data class DeleteDTO(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: Any?
)