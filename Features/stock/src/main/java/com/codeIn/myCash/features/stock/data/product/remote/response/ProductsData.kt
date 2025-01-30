package com.codeIn.myCash.features.stock.data.product.remote.response

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class ProductsData (
    @SerializedName("data")
    val `data`: List<ProductModel>?,
    @SerializedName("pagination")
    val pagination: Pagination
)