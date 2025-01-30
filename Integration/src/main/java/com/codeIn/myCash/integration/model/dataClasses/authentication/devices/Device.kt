package com.codeIn.myCash.integration.model.dataClasses.authentication.devices

import com.google.gson.annotations.SerializedName


data class Device (

    @SerializedName("id"             ) var id             : Int?                      = null,
    @SerializedName("name"           ) var name           : String?                   = null,
    @SerializedName("image"          ) var image          : String?                   = null,
    @SerializedName("status"         ) var status         : Int?                      = null,
    @SerializedName("model"          ) var model          : String?                   = null,
    @SerializedName("description_ar" ) var descriptionAr  : String?                   = null,
    @SerializedName("description_en" ) var descriptionEn  : String?                   = null,
    @SerializedName("name_ar"        ) var nameAr         : String?                   = null,
    @SerializedName("name_en"        ) var nameEn         : String?                   = null,
    @SerializedName("brand"          ) var brand          : Brand?                    = Brand(),
    @SerializedName("deviceFeatures" ) var deviceFeatures : ArrayList<DeviceFeatures> = arrayListOf(),
    @SerializedName("devicePaper"    ) var devicePaper    : ArrayList<DevicePaper>    = arrayListOf(),
    @SerializedName("deviceFont"     ) var deviceFont     : ArrayList<DeviceFont>     = arrayListOf()

)