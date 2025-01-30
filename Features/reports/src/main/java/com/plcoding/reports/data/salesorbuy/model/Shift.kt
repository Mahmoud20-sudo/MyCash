package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Shift(
    @SerializedName("branch")
    val branch: Any,
    @SerializedName("currentCash")
    val currentCash: String,
    @SerializedName("currentVisa")
    val currentVisa: String,
    @SerializedName("differentInCash")
    val differentInCash: String,
    @SerializedName("differentInVisa")
    val differentInVisa: String,
    @SerializedName("endCash")
    val endCash: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("endVisa")
    val endVisa: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("startCash")
    val startCash: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("startVisa")
    val startVisa: String,
    @SerializedName("statistics")
    val statistics: Statistics
)