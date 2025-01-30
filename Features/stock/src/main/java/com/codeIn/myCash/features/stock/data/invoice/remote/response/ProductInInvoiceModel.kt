package com.codeIn.myCash.features.stock.data.invoice.remote.response

import android.os.Parcelable
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductInInvoiceModel(
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("discountPrice")
    val discountPrice: String?,
    @SerializedName("discountType")
    val discountType: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invoiceDiscount")
    val invoiceDiscount: String?,
    @SerializedName("invoiceDiscountType")
    val invoiceDiscountType: String?,
    @SerializedName("invoiceDiscountValue")
    val invoiceDiscountValue: String?,
    @SerializedName("product")
    val product: ProductModel?,
    @SerializedName("productPrice")
    val productPrice: String?,
    @SerializedName("quantity")
    val quantity: String?,
    @SerializedName("taxPrice")
    val taxPrice: String?,
    @SerializedName("totalPrice")
    val totalPrice: String? ,
    @SerializedName("UnitPrice")
    val unitPrice: String? ,
    @SerializedName("unitDiscountPrice")
    val unitDiscountPrice: String? ,
    @SerializedName("hasNotification")
    val hasNotification: String? ,
    @SerializedName("notificationPrice")
    val notificationPrice: String? ,


    @SerializedName("totalAfterNotification")
    val totalAfterNotification: String?,


    var count : Int =0
) : Parcelable