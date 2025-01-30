package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class MainBranch(
    @SerializedName("additional_info")
    val additionalInfo: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isMain")
    val isMain: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("status")
    val status: Int
)