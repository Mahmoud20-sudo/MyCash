package com.codeIn.myCash.features.stock.data.product.remote.response

import com.google.gson.annotations.SerializedName

data class DetailsProductDTO(
    @SerializedName("data")
    val data: ProductModel?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)