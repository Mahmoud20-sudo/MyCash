package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Cat(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("parent_id")
    val parentId: Int,
    @SerializedName("parent_name")
    val parentName: String,
    @SerializedName("selected")
    val selected: Boolean,
    @SerializedName("subCategories")
    val subCategories: List<Any>
)