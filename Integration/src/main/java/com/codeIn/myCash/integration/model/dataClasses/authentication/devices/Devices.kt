package com.codeIn.myCash.integration.model.dataClasses.authentication.devices

import com.google.gson.annotations.SerializedName


data class Devices (

    @SerializedName("data"       ) var data       : ArrayList<DeviceWrapper> = arrayListOf(),
    @SerializedName("pagination" ) var pagination : com.codeIn.myCash.integration.model.dataClasses.Pagination?              = com.codeIn.myCash.integration.model.dataClasses.Pagination()

)