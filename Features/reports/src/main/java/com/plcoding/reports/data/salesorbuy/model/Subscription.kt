package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Subscription(
    @SerializedName("daysLeft")
    val daysLeft: Int,
    @SerializedName("device")
    val device: Device,
    @SerializedName("devicePrice")
    val devicePrice: String,
    @SerializedName("deviceToken")
    val deviceToken: Any,
    @SerializedName("discountPrice")
    val discountPrice: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("expire")
    val expire: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("influencer")
    val influencer: Any,
    @SerializedName("packagePrice")
    val packagePrice: String,
    @SerializedName("package")
    val packageX: Package,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("taxPrice")
    val taxPrice: String,
    @SerializedName("totalPrice")
    val totalPrice: String
)