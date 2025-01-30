package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class ReportsDTO(
    @SerializedName("data")
    val saleOrBuyData: SaleOrBuyData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)