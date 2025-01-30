package com.codeIn.myCash.ui.home.products.products

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category (

    @SerializedName("id"            ) var id            : Int?              = null,
    @SerializedName("selected"      ) var selected      : Boolean?          = null,
    @SerializedName("name"          ) var name          : String?           = null,
    @SerializedName("parent_id"     ) var parentId      : Int?              = null,
    @SerializedName("subCategories" ) var subCategories : ArrayList<String> = arrayListOf()

) : Parcelable


@Parcelize
data class Product (
    @SerializedName("count"                   ) var count                   : Int    = 0,
    @SerializedName("id"                      ) var id                      : Int?    = null,
    @SerializedName("name"                    ) var name                    : String? = null,
    @SerializedName("desc"                    ) var desc                    : String? = null,
    @SerializedName("taxAvailable"            ) var taxAvailable            : Int?    = null,
    @SerializedName("isQuick"                 ) var isQuick                 : Int?    = null,
    @SerializedName("quantity"                ) var quantity                : Int?    = null,
    @SerializedName("status"                  ) var status                  : Int?    = null,
    @SerializedName("hasDiscount"             ) var hasDiscount             : Int?    = null,
    @SerializedName("discountType"            ) var discountType            : Int?    = null,
    @SerializedName("discount"                ) var discount                : String? = null,
    @SerializedName("discountPrice"           ) var discountPrice           : Int?    = null,
    @SerializedName("priceAfterDiscount"      ) var priceAfterDiscount      : String? = null,
    @SerializedName("price"                   ) var price                   : String? = null,
    @SerializedName("finalPrice"              ) var finalPrice              : String? = null,
    @SerializedName("finalPriceAfterDiscount" ) var finalPriceAfterDiscount : String? = null,
    @SerializedName("tax"                     ) var tax                     : String? = null,
    @SerializedName("buyPrice"                ) var buyPrice                : String? = null,
    @SerializedName("barCode"                 ) var barCode                 : String? = null,
    @SerializedName("cat"                     ) var cat                     : Category?    = Category(),
    @SerializedName("cat_id"                  ) var catId                   : Int?    = null,
    @SerializedName("parentCat_id"            ) var parentCatId             : Int?    = null,
    @SerializedName("image"                   ) var image                   : String? = null,
    @SerializedName("inCart"                  ) var inCart                  : Int?    = null,
    @SerializedName("cart"                    ) var cart                    : String? = null

) : Parcelable

data class SelectedProducts (
    @SerializedName("count"        ) var count           : Int            = 0,
    @SerializedName("totalPrice"   ) var totalPrice      : Double         = 0.0
)
