package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Product(
    @SerializedName("discount")
    val discount: String,
    @SerializedName("discountPrice")
    val discountPrice: String,
    @SerializedName("discountType")
    val discountType: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invoiceDiscount")
    val invoiceDiscount: String,
    @SerializedName("invoiceDiscountType")
    val invoiceDiscountType: String,
    @SerializedName("invoiceDiscountValue")
    val invoiceDiscountValue: String,
    @SerializedName("product")
    val product: ProductX,
    @SerializedName("productPrice")
    val productPrice: String,
    @SerializedName("quantity")
    val quantity: String,
    @SerializedName("returnedQuantity")
    val returnedQuantity: String,
    @SerializedName("taxPrice")
    val taxPrice: String,
    @SerializedName("totalPrice")
    val totalPrice: String,
    @SerializedName("unitDiscountPrice")
    val unitDiscountPrice: String,
    @SerializedName("UnitPrice")
    val unitPrice: String
)