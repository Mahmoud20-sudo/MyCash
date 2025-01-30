package com.codeIn.myCash.features.stock.data.memorandum.remote.response

import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.google.gson.annotations.SerializedName

data class MemorandumModel(
    @SerializedName("cash")
    val cash: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("products")
    val products: List<ProductModelInMemorandum>?,
    @SerializedName("type")
    val type: Int,
    @SerializedName("visa")
    val visa: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("price")
    val price : String? ,
    @SerializedName("taxPrice")
    val taxPrice : String? ,
    @SerializedName("totalPrice")
    val totalPrice : String? ,
    @SerializedName("paymentType")
    val paymentType : String? ,
    @SerializedName("invoice")
    val invoiceModel : InvoiceModel
)