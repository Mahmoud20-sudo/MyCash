package com.codeIn.myCash.integration.model.dataClasses.authentication.packages

import com.google.gson.annotations.SerializedName

data class Features (

    @SerializedName("id"   ) var id   : Int?    = null,
    @SerializedName("name" ) var name : String? = null

)