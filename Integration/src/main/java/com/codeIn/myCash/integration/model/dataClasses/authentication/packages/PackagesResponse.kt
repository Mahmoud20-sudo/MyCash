package com.codeIn.myCash.integration.model.dataClasses.authentication.packages

import com.google.gson.annotations.SerializedName


data class PackagesResponse (

  @SerializedName("status"  ) var status  : Int?    = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("data"    ) var packages    : Packages?   = Packages()

)