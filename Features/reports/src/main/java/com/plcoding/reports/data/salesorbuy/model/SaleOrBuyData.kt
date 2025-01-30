package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class SaleOrBuyData(
    @SerializedName("invoices")
    val invoices: Invoices,
    @SerializedName("report")
    val report: Report
)