package com.codeIn.myCash.features.stock.data.category.remote.response.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("parent_id")
    val parentId: String? = null ,
    @SerializedName("parent_name")
    val parentName: String? = null ,
    @SerializedName("selected")
    var selected: Boolean = false ,
    @SerializedName("subCategories")
    val subCategories: List<CategoryData>? = null
) : Parcelable
