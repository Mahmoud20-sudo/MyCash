package com.codeIn.myCash.integration.model.dataClasses.authentication.devices

import com.google.gson.annotations.SerializedName


data class DevicesResponse (

  @SerializedName("status"  ) var status  : Int?    = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("data"    ) var devices    : Devices?   = Devices()

)