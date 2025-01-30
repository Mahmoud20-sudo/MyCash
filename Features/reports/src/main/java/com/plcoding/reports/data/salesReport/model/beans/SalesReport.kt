package com.plcoding.reports.data.salesReport.model.beans


import com.google.gson.annotations.SerializedName

data class SalesReport(
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("invoice_count")
    val invoiceCount: Int,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("totalPrice")
    val totalPrice: Double,
    @SerializedName("total_sales_with_out_tax")
    val totalSalesWithOutTax: Double,
    @SerializedName("total_sales_with_tax")
    val totalSalesWithTax: Double,
    @SerializedName("total_cash")
    val totalCash: Double,
    @SerializedName("total_visa")
    val totalVisa: Double,
    @SerializedName("total_remaining")
    val totalRemaining: Double,

)