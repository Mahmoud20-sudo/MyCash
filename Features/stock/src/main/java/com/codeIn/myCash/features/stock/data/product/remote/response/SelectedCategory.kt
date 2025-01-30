package com.codeIn.myCash.features.stock.data.product.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedCategory(
    @SerializedName("id")
    val id : Int,
    @SerializedName("selected")
    val selected : Boolean,
    @SerializedName("parent_name")
    val parentName : String? ,
    @SerializedName("name")
    val name : String? ,
    @SerializedName("parent_id")
    val parentId : Int ,
    @SerializedName("subCategories")
    val subCategories : List<SelectedCategory>?
) : Parcelable
