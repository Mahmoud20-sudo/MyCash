package com.codeIn.myCash.integration.model.dataClasses.authentication.packages

import com.google.gson.annotations.SerializedName


data class Package (

    @SerializedName("id"              ) var id              : Int?              = null,
    @SerializedName("name"            ) var name            : String?           = null,
    @SerializedName("image"           ) var image           : String?           = null,
    @SerializedName("name_ar"         ) var nameAr          : String?           = null,
    @SerializedName("name_en"         ) var nameEn          : String?           = null,
    @SerializedName("duration"        ) var duration        : String?           = null,
    @SerializedName("duration_ar"     ) var durationAr      : String?           = null,
    @SerializedName("duration_en"     ) var durationEn      : String?           = null,
    @SerializedName("desc"            ) var desc            : String?           = null,
    @SerializedName("desc_ar"         ) var descAr          : String?           = null,
    @SerializedName("desc_en"         ) var descEn          : String?           = null,
    @SerializedName("isDiscount"      ) var isDiscountAvailable: String?           = null,
    @SerializedName("discount"        ) var discount        : String?           = null,
    @SerializedName("discountType"    ) var discountType    : String?           = null,
    @SerializedName("price"           ) var price           : String?           = null,
    @SerializedName("finalPrice"      ) var finalPrice      : String?           = null,
    @SerializedName("days"            ) var days            : String?           = null,
    @SerializedName("usersCount"      ) var usersCount      : Int?              = null,
    @SerializedName("offlineOrOnline" ) var offlineOrOnline : String?           = null,
    @SerializedName("country_id"      ) var countryId       : String?           = null,
    @SerializedName("features"        ) var features        : ArrayList<Features> = arrayListOf()

)