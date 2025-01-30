package com.codeIn.myCash.features.stock.data.category.remote.response.category

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class CategoriesData(
    @SerializedName("data")
    val `data`: List<CategoryData>,
    @SerializedName("pagination")
    val pagination: Pagination
)
