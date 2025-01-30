package com.codeIn.myCash.integration.model.dataClasses.authentication.user

import com.google.gson.annotations.SerializedName


data class MainBranch (

    @SerializedName("id"          ) var id         : Int?    = null,
    @SerializedName("name"        ) var name       : String? = null,
    @SerializedName("address"     ) var address    : String? = null,
    @SerializedName("isMain"      ) var isMain     : String? = null,
    @SerializedName("user_id"     ) var userId     : Int?    = null,
    @SerializedName("employee_id" ) var employeeId : String? = null

)