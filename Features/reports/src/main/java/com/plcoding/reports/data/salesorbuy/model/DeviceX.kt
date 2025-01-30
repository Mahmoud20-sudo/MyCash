package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class DeviceX(
    @SerializedName("brand")
    val brand: Brand,
    @SerializedName("description_ar")
    val descriptionAr: String,
    @SerializedName("description_en")
    val descriptionEn: String,
    @SerializedName("deviceFeatures")
    val deviceFeatures: List<DeviceFeature>,
    @SerializedName("deviceFont")
    val deviceFont: List<DeviceFont>,
    @SerializedName("devicePaper")
    val devicePaper: List<DevicePaper>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_ar")
    val nameAr: String,
    @SerializedName("name_en")
    val nameEn: String,
    @SerializedName("type")
    val type: Int
)