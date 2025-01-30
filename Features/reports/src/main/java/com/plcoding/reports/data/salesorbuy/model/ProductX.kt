package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class ProductX(
    @SerializedName("barCode")
    val barCode: String,
    @SerializedName("buyPrice")
    val buyPrice: String,
    @SerializedName("buyTaxAvailable")
    val buyTaxAvailable: Int,
    @SerializedName("buyTaxPrice")
    val buyTaxPrice: String,
    @SerializedName("buyTaxType")
    val buyTaxType: Int,
    @SerializedName("cat")
    val cat: Cat,
    @SerializedName("cats")
    val cats: Any,
    @SerializedName("date")
    val date: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("discount")
    val discount: Int,
    @SerializedName("discountPrice")
    val discountPrice: String,
    @SerializedName("discountType")
    val discountType: Int,
    @SerializedName("finalBuyPrice")
    val finalBuyPrice: String,
    @SerializedName("finalPrice")
    val finalPrice: String,
    @SerializedName("hasDiscount")
    val hasDiscount: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("inCart")
    val inCart: Boolean,
    @SerializedName("isQuick")
    val isQuick: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("parentCat_id")
    val parentCatId: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("productBuyPrice")
    val productBuyPrice: String,
    @SerializedName("productPrice")
    val productPrice: String,
    @SerializedName("productPriceAfterDiscount")
    val productPriceAfterDiscount: String,
    @SerializedName("quantity")
    val quantity: String,
    @SerializedName("taxAvailable")
    val taxAvailable: Int,
    @SerializedName("taxPrice")
    val taxPrice: String,
    @SerializedName("taxType")
    val taxType: Int
)