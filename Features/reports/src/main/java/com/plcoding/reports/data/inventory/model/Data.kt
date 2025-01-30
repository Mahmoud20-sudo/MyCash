package com.plcoding.reports.data.inventory.model

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class Data (

  @SerializedName("data"       ) var data       : ArrayList<InventoryModel> = arrayListOf(),
  @SerializedName("pagination" ) var pagination : Pagination?     = Pagination()

)