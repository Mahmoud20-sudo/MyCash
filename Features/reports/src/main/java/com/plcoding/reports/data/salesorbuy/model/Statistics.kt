package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Statistics(
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("startDay")
    val startDay: String,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("totalPrice")
    val totalPrice: Int
)