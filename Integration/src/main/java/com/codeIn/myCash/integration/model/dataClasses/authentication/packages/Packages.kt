package com.codeIn.myCash.integration.model.dataClasses.authentication.packages

import com.codeIn.myCash.integration.model.dataClasses.Pagination
import com.google.gson.annotations.SerializedName


data class Packages (

    @SerializedName("data"       ) var data       : ArrayList<Package> = arrayListOf(),
    @SerializedName("pagination" ) var pagination : Pagination?        = Pagination()

)

