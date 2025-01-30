package com.codeIn.myCash.features.stock.data.product.remote.response

import android.os.Parcelable
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData
import com.codeIn.myCash.features.stock.domain.invoice.model.DiscountInInvoiceModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductModel(
    @SerializedName("barCode")
    val barCode: String?,
    @SerializedName("cat")
    val category: CategoryData? = null ,
    @SerializedName("desc")
    val description: String?,
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("discountPrice")
    var discountPrice: String?,
    @SerializedName("discountType")
    val discountType: String?,
    @SerializedName("finalPrice")
    var finalPrice: String?,
    @SerializedName("hasDiscount")
    val hasDiscount: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String?,
    @SerializedName("inCart")
    val inCart: Boolean,
    @SerializedName("name")
    val name: String?,
    @SerializedName("parentCat_id")
    val parentCategoryId: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("buyPrice")
    val buyPrice: String?,
    @SerializedName("productPrice")
    val productPrice: String?,
    @SerializedName("productPriceAfterDiscount")
    var productPriceAfterDiscount: String?,
    @SerializedName("quantity")
    val quantity: String?,
    @SerializedName("taxAvailable")
    val taxAvailable: String?,
    @SerializedName("taxPrice")
    var taxPrice: String?,
    @SerializedName("taxType")
    val taxType: String?,
    @SerializedName("cats")
    val selectedCategories : List<SelectedCategory>? ,
    @SerializedName("buyTaxType")
    val buyTaxType: String?,
    @SerializedName("buyTaxAvailable")
    val buyTaxAvailable: String?,
    @SerializedName("buyTaxPrice")
    val buyTaxPrice: String?,
    @SerializedName("finalBuyPrice")
    val finalBuyPrice: String?,
    @SerializedName("productBuyPrice")
    val productBuyPrice: String?,
    @SerializedName("hasNotification")
    val hasNotification: String?,
    @SerializedName("branch_id")
    val branch: String?,

    @SerializedName("totalAfterNotification")
    val totalAfterNotification: String?,

    var count : Int = 0 ,
    var difPrice : String? = "" ,
    var discountInInvoiceModel : DiscountInInvoiceModel?= null
) : Parcelable