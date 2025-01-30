package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Package(
    @SerializedName("country_id")
    val countryId: Int,
    @SerializedName("days")
    val days: Int,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("desc_ar")
    val descAr: String,
    @SerializedName("desc_en")
    val descEn: String,
    @SerializedName("discount")
    val discount: Int,
    @SerializedName("discountType")
    val discountType: Int,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("duration_ar")
    val durationAr: String,
    @SerializedName("duration_en")
    val durationEn: String,
    @SerializedName("features")
    val features: List<Feature>,
    @SerializedName("finalPrice")
    val finalPrice: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("isDiscount")
    val isDiscount: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_ar")
    val nameAr: String,
    @SerializedName("name_en")
    val nameEn: String,
    @SerializedName("offlineOrOnline")
    val offlineOrOnline: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("usersCount")
    val usersCount: Int
)