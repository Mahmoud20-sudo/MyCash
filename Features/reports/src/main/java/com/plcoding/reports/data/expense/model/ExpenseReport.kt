package com.plcoding.reports.data.expense.model


import com.google.gson.annotations.SerializedName

data class ExpenseReport(
    @SerializedName("total_price")
    val totalPrice: Double? = null,
    @SerializedName("total_tax")
    val total_tax: Double? = null,
    @SerializedName("total_without_tax")
    val totalWithoutTax: Double? = null
){
    val totalPriceWithTax get() =  totalPrice
}