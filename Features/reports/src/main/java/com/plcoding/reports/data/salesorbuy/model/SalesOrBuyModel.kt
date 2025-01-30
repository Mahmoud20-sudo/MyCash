package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class SalesOrBuyModel(
    @SerializedName("cashPrice")
    val cashPrice: String,
    @SerializedName("change_csahir_price")
    val changeCsahirPrice: String,
    @SerializedName("client")
    val client: Client? = null,
    @SerializedName("codeRefund")
    val codeRefund: Any,
    @SerializedName("date")
    val date: String,
    @SerializedName("dateRefund")
    val dateRefund: Any,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("discountPrice")
    val discountPrice: String,
    @SerializedName("discountType")
    val discountType: Int,
    @SerializedName("hasInvoiceNotification")
    val hasInvoiceNotification: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invoiceNumber")
    val invoiceNumber: Int,
    @SerializedName("invoiceOrder")
    val invoiceOrder: Int,
    @SerializedName("invoiceType")
    val invoiceType: Int,
    @SerializedName("isPayVisa")
    val isPayVisa: Int,
    @SerializedName("isReturn")
    val isReturn: Int,
    @SerializedName("nextData")
    val nextData: String,
    @SerializedName("paid_cashir_price")
    val paidCashirPrice: String,
    @SerializedName("paymentStatus")
    val paymentStatus: Int,
    @SerializedName("paymentType")
    val paymentType: Int,
    @SerializedName("productPrice")
    val productPrice: String,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("rammingPrice")
    val rammingPrice: String,
    @SerializedName("runRefund")
    val runRefund: Any,
    @SerializedName("saleOrBuy")
    val saleOrBuy: Int,
    @SerializedName("shift")
    val shift: Shift,
    @SerializedName("tax")
    val tax: String,
    @SerializedName("taxPrice")
    val taxPrice: String,
    @SerializedName("totalPrice")
    val totalPrice: String,
    @SerializedName("userDate")
    val userDate: UserDate,
    @SerializedName("visaPrice")
    val visaPrice: String,
    @SerializedName("visaType")
    val visaType: Any,
    @SerializedName("zatka")
    val zatka: String
){
    val totalWithoutTax get() = productPrice
    val totalPriceWithTax get() = totalPrice
}