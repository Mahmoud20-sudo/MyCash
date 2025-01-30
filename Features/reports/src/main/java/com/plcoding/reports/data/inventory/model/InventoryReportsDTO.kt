package com.plcoding.reports.data.inventory.model

import com.google.gson.annotations.SerializedName


data class InventoryReportsDTO (

  @SerializedName("status"  ) var status  : Int?    = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("data"    ) var data    : Data?   = Data()

)