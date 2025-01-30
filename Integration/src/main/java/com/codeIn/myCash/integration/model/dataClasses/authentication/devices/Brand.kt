package com.codeIn.myCash.integration.model.dataClasses.authentication.devices

import com.google.gson.annotations.SerializedName


data class Brand (

  @SerializedName("id"         ) var id        : Int?    = null,
  @SerializedName("name"       ) var name      : String? = null,
  @SerializedName("icon"       ) var icon      : String? = null,
  @SerializedName("status"     ) var status    : Int?    = null,
  @SerializedName("country_id" ) var countryId : String? = null,
  @SerializedName("name_ar"    ) var nameAr    : String? = null,
  @SerializedName("name_en"    ) var nameEn    : String? = null

)