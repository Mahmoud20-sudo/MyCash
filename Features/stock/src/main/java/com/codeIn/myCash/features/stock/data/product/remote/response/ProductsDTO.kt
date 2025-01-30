package com.codeIn.myCash.features.stock.data.product.remote.response

import com.google.gson.annotations.SerializedName

data class ProductsDTO (
    @SerializedName("data")
    val `data`: ProductsData,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)