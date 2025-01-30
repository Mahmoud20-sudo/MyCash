package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Brand(
    @SerializedName("country_id")
    val countryId: Int,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_ar")
    val nameAr: String,
    @SerializedName("name_en")
    val nameEn: String,
    @SerializedName("status")
    val status: Int
)