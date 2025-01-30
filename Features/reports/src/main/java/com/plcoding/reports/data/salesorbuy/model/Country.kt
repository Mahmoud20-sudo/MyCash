package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Country(
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("currency_ar")
    val currencyAr: String,
    @SerializedName("currency_en")
    val currencyEn: String,
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
    @SerializedName("phoneNumbers")
    val phoneNumbers: Int,
    @SerializedName("status")
    val status: Int
)