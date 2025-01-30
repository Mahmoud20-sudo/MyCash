package com.plcoding.reports.data.postPaid.model.beans


import com.google.gson.annotations.SerializedName

data class Postpaid(
    @SerializedName("branch_name")
    val branchName: String,
    @SerializedName("date_of_payment")
    val dateOfPayment: String,
    @SerializedName("invoice_number")
    val invoiceNumber: Int,
    @SerializedName("receipt_amount")
    val receiptAmount: Double,
    @SerializedName("receipt_id")
    val receiptId: Int,
    @SerializedName("receipt_number")
    val receiptNumber: Int,
    @SerializedName("receipt_state")
    val receiptState: String,
    @SerializedName("total_invoice")
    val totalInvoice: Double
)