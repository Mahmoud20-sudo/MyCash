package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class DevicePaper(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name_ar")
    val nameAr: String,
    @SerializedName("name_en")
    val nameEn: String
)