package com.codeIn.myCash.integration.model.dataClasses.authentication.user

import com.google.gson.annotations.SerializedName


data class AccountInfo (

    @SerializedName("id"                   ) var id                   : Int?    = null,
    @SerializedName("taxRecord"            ) var taxRecord            : String? = null,
    @SerializedName("commercialRecord"     ) var commercialRecord     : String? = null,
    @SerializedName("commercialRecordName" ) var commercialRecordName : String? = null,
    @SerializedName("tax"                  ) var tax                  : String? = null,
    @SerializedName("notification"         ) var notification         : Int?    = null,
    @SerializedName("drafts"               ) var drafts               : Int?    = null,
    @SerializedName("quickInvoice"         ) var quickInvoice         : Int?    = null,
    @SerializedName("logo"                 ) var logo                 : String? = null

)