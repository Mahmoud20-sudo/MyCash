package com.plcoding.reports.data.salesorbuy.model


import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName


data class Invoices(
    @SerializedName("data")
    val `data`: ArrayList<SalesOrBuyModel>,
    @SerializedName("pagination")
    val pagination: Pagination
)