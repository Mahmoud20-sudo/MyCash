package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Device(
    @SerializedName("device")
    val device: DeviceX,
    @SerializedName("discount")
    val discount: Any,
    @SerializedName("discountType")
    val discountType: Any,
    @SerializedName("finalPrice")
    val finalPrice: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isDiscount")
    val isDiscount: Any,
    @SerializedName("price")
    val price: String
)