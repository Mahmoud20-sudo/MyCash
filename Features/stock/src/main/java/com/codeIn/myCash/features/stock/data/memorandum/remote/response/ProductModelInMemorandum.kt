package com.codeIn.myCash.features.stock.data.memorandum.remote.response

import android.os.Parcelable
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModelInMemorandum(
    @SerializedName("id")
    val id: Int,
    @SerializedName("price")
    val price: String?,
    @SerializedName("product")
    val product: ProductModel?,
    @SerializedName("quantity")
    val quantity: String?,
    @SerializedName("tax")
    val tax: String?,
    @SerializedName("taxValue")
    val taxValue: String?,
    @SerializedName("totalPrice")
    val totalPrice: String? ,
) : Parcelable