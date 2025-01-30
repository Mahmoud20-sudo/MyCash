package com.codeIn.myCash.features.stock.data.invoice.remote.response

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class InvoicesData (
    @SerializedName("data")
    val `data`: List<InvoiceModel>,
    @SerializedName("pagination")
    val pagination: Pagination
)