package com.codeIn.myCash.features.stock.data.category.remote.response.category

import com.google.gson.annotations.SerializedName


data class CategoriesDTO(
    @SerializedName("data")
    val `data`: CategoriesData,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)

