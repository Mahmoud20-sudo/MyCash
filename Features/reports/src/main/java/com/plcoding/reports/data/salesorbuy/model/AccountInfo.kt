package com.plcoding.reports.data.salesorbuy.model

import com.google.gson.annotations.SerializedName

data class AccountInfo(
    @SerializedName("commercialRecord")
    val commercialRecord: String,
    @SerializedName("commercialRecordName")
    val commercialRecordName: String,
    @SerializedName("drafts")
    val drafts: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("notification")
    val notification: Int,
    @SerializedName("quickInvoice")
    val quickInvoice: Int,
    @SerializedName("tax")
    val tax: String,
    @SerializedName("taxRecord")
    val taxRecord: String
)