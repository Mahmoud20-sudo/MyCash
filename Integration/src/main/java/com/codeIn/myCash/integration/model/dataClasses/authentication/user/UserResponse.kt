package com.codeIn.myCash.integration.model.dataClasses.authentication.user

import com.google.gson.annotations.SerializedName


data class UserResponse (

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : User?   = User()

)