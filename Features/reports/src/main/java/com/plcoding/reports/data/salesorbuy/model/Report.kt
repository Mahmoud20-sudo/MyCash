package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Report(
    @SerializedName("total_price")
    val totalPrice: Double,
    @SerializedName("total_tax")
    val totalTax: Double,
    @SerializedName("total_without_tax")
    val totalWithoutTax: Double
){
    val totalPriceWithTax get() = totalPrice
}