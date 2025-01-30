package com.codeIn.myCash.integration.model.dataClasses

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class GeneralResponse (

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null

)
object ErrorConverter{
    inline fun <reified T> convert(errorBody: String?): T {
        return Gson().fromJson(errorBody ?: "{\"status\":0,\"message\":\"An error occurred\",\"data\":null}", T::class.java)
    }
}