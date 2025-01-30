package com.codeIn.myCash.integration.model.dataClasses.authentication.devices

import com.google.gson.annotations.SerializedName


data class DeviceWrapper (

    @SerializedName("id"     ) var id     : Int?    = null,
    @SerializedName("price"  ) var price  : String? = null,
    @SerializedName("device" ) var device : Device? = Device(),
    var isSelected : Boolean = false

)