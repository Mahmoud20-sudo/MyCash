package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Feature(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_ar")
    val nameAr: String,
    @SerializedName("name_en")
    val nameEn: String
)