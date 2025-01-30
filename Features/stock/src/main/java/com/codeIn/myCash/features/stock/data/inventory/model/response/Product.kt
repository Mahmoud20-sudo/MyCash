package com.codeIn.stock.inventory.data.model.response

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("quantity")
    val quantity: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("buyPrice")
    val buyPrice: String?,
    @SerializedName("productPrice")
    val productPrice: String?,
    @SerializedName("productPriceAfterDiscount")
    val productPriceAfterDiscount: String?,
    @SerializedName("discountPrice")
    val discountPrice: String?,
    @SerializedName("taxPrice")
    val taxPrice: String?,
    @SerializedName("finalPrice")
    val finalPrice: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("inCart")
    val inCart: Boolean?,
    @SerializedName("hasDiscount")
    val hasDiscount: Int?,
    @SerializedName("discount")
    val discount: Int?,
    @SerializedName("discountType")
    val discountType: Int?,
    @SerializedName("taxAvailable")
    val taxAvailable: Int?,
    @SerializedName("taxType")
    val taxType: Int?,
    @SerializedName("desc")
    val desc: String?,
    @SerializedName("barCode")
    val barCode: String?,
    @SerializedName("isQuick")
    val isQuick: Any?,
    @SerializedName("cat")
    val cat: Cat?,
    @SerializedName("parentCat_id")
    val parentCatId: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("cats")
    val cats: Any?
) {
    data class Cat(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("selected")
        val selected: Boolean?,
        @SerializedName("parent_name")
        val parentName: Any?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("parent_id")
        val parentId: Int?,
        @SerializedName("subCategories")
        val subCategories: List<Any?>?
    )
}