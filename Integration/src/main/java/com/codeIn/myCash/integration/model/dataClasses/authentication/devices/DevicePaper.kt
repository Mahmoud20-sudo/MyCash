package com.codeIn.myCash.integration.model.dataClasses.authentication.devices

import com.google.gson.annotations.SerializedName


data class DevicePaper (

  @SerializedName("id"      ) var id     : Int?    = null,
  @SerializedName("name_ar" ) var nameAr : String? = null,
  @SerializedName("name_en" ) var nameEn : String? = null

)