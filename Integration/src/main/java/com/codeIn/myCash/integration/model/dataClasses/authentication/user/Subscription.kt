package com.codeIn.myCash.integration.model.dataClasses.authentication.user

import com.codeIn.myCash.integration.model.dataClasses.authentication.devices.Device
import com.codeIn.myCash.integration.model.dataClasses.authentication.packages.Package
import com.google.gson.annotations.SerializedName


data class Subscription (

    @SerializedName("id"            ) var id            : Int?     = null,
    @SerializedName("startDate"     ) var startDate     : String?  = null,
    @SerializedName("endDate"       ) var endDate       : String?  = null,
    @SerializedName("daysLeft"      ) var daysLeft      : Int?     = null,
    @SerializedName("expire"        ) var expire        : Int?     = null,
    @SerializedName("deviceToken"   ) var deviceToken   : String?  = null,
    @SerializedName("packagePrice"  ) var packagePrice  : String?  = null,
    @SerializedName("devicePrice"   ) var devicePrice   : String?  = null,
    @SerializedName("discountPrice" ) var discountPrice : String?  = null,
    @SerializedName("taxPrice"      ) var taxPrice      : String?  = null,
    @SerializedName("totalPrice"    ) var totalPrice    : String?  = null,
    @SerializedName("device"        ) var device        : Device?  = Device(),
    @SerializedName("package"       ) var currentPackage: Package? = Package(),
    @SerializedName("influencer"    ) var influencer    : String?  = null

)