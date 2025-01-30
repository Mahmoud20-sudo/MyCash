package com.plcoding.reports.data.inventory.model

import com.google.gson.annotations.SerializedName


data class Product (

  @SerializedName("id"                        ) var id                        : Int?     = null,
  @SerializedName("quantity"                  ) var quantity                  : String?  = null,
  @SerializedName("price"                     ) var price                     : String?  = null,
  @SerializedName("buyPrice"                  ) var buyPrice                  : String?  = null,
  @SerializedName("productPrice"              ) var productPrice              : String?  = null,
  @SerializedName("productBuyPrice"           ) var productBuyPrice           : String?  = null,
  @SerializedName("productPriceAfterDiscount" ) var productPriceAfterDiscount : String?  = null,
  @SerializedName("discountPrice"             ) var discountPrice             : String?  = null,
  @SerializedName("taxPrice"                  ) var taxPrice                  : String?  = null,
  @SerializedName("finalPrice"                ) var finalPrice                : String?  = null,
  @SerializedName("name"                      ) var name                      : String?  = null,
  @SerializedName("inCart"                    ) var inCart                    : Boolean? = null,
  @SerializedName("hasDiscount"               ) var hasDiscount               : Int?     = null,
  @SerializedName("discount"                  ) var discount                  : Int?     = null,
  @SerializedName("discountType"              ) var discountType              : Int?     = null,
  @SerializedName("taxAvailable"              ) var taxAvailable              : Int?     = null,
  @SerializedName("buyTaxAvailable"           ) var buyTaxAvailable           : Int?     = null,
  @SerializedName("buyTaxPrice"               ) var buyTaxPrice               : String?  = null,
  @SerializedName("finalBuyPrice"             ) var finalBuyPrice             : String?  = null,
  @SerializedName("taxType"                   ) var taxType                   : Int?     = null,
  @SerializedName("buyTaxType"                ) var buyTaxType                : Int?     = null,
  @SerializedName("desc"                      ) var desc                      : String?  = null,
  @SerializedName("barCode"                   ) var barCode                   : String?  = null,
  @SerializedName("isQuick"                   ) var isQuick                   : String?  = null,
  @SerializedName("date"                      ) var date                      : String?  = null,
  @SerializedName("cat"                       ) var cat                       : Cat?     = Cat(),
  @SerializedName("parentCat_id"              ) var parentCatId               : Int?     = null,
  @SerializedName("image"                     ) var image                     : String?  = null,
  @SerializedName("cats"                      ) var cats                      : String?  = null

)