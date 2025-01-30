package com.plcoding.reports.data.returnReport.model.beans


data class ReturnInvoice(
    val id: String,
    val date: String,
    val dateRefund: String,
    val invoiceType: Int,
    val paymentType: Int,
    val returnedPrice: Double,
    val totalPrice: Double,
    val isReturn: String?,
    val type: String?,
)