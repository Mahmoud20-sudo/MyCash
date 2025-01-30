package com.codeIn.myCash.ui.options.notifications

import com.google.gson.annotations.SerializedName


data class Notification (

    @SerializedName("id"                 ) var id                 : Int?                = null,
    @SerializedName("title "             ) var title              : String?                = null,
    @SerializedName("subTitle"           ) var subTitle           : String?            = null,
    @SerializedName("date"               ) var date               : String?             = null,
    @SerializedName("type"               ) var type               : Int?                = null

)