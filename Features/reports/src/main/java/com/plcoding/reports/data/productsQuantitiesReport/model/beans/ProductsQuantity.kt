package com.plcoding.reports.data.productsQuantitiesReport.model.beans

import com.google.gson.annotations.SerializedName

data class ProductsQuantity(
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("branch_name")
    val branchName: String,
    @SerializedName("category_name")
    val category: String,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("quantity")
    val quantity: Int
)



