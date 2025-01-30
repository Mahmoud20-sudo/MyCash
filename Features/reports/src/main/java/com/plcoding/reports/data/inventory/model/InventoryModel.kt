package com.plcoding.reports.data.inventory.model

import com.google.gson.annotations.SerializedName


data class InventoryModel (

  @SerializedName("id"             ) var id             : Int?     = null,
  @SerializedName("allQuantity"    ) var allQuantity    : Int?     = null,
  @SerializedName("quantity"       ) var quantity       : Int?     = null,
  @SerializedName("damageQuantity" ) var damageQuantity : Int?     = null,
  @SerializedName("created_at"     ) var createdAt      : String?  = null,
  @SerializedName("product"        ) var product        : Product? = Product()

)