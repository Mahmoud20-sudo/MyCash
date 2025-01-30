package com.codeIn.myCash.integration.model.dataClasses.authentication.user


import com.google.gson.annotations.SerializedName


data class Country (

    @SerializedName("id"           ) var id           : Int?    = null,
    @SerializedName("name"         ) var name         : String? = null,
    @SerializedName("currency"     ) var currency     : String? = null,
    @SerializedName("icon"         ) var icon         : String? = null,
    @SerializedName("status"       ) var status       : Int?    = null,
    @SerializedName("countryCode"  ) var countryCode  : String? = null,
    @SerializedName("phoneNumbers" ) var phoneNumbers : String? = null,
    @SerializedName("name_ar"      ) var nameAr       : String? = null,
    @SerializedName("name_en"      ) var nameEn       : String? = null,
    @SerializedName("currency_ar"  ) var currencyAr   : String? = null,
    @SerializedName("currency_en"  ) var currencyEn   : String? = null

)
