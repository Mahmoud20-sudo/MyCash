package com.codeIn.myCash.integration.model.dataClasses.authentication.user

import com.google.gson.annotations.SerializedName

data class User (

    @SerializedName("id"                    ) var id                    : Int?          = null,
    @SerializedName("name"                  ) var name                  : String?       = null,
    @SerializedName("phone"                 ) var phone                 : String?       = null,
    @SerializedName("email"                 ) var email                 : String?       = null,
    @SerializedName("type"                  ) var type                  : Int?          = null,
    @SerializedName("status"                ) var status                : Int?          = null,
    @SerializedName("msgCode"               ) var msgCode               : Int?          = null,
    @SerializedName("invoicesCount"         ) var invoicesCount         : Int?          = null,
    @SerializedName("paymentStatus"         ) var paymentStatus         : String?       = null,
    @SerializedName("isCompleteAccountInfo" ) var isCompleteAccountInfo : String?       = null,
    @SerializedName("isCompleteShitInfo"    ) var isCompleteShitInfo    : Int?          = null,
    @SerializedName("has_permission"        ) var hasPermission         : Int?          = null,
    @SerializedName("lang"                  ) var lang                  : String?       = null,
    @SerializedName("firebase"              ) var firebase              : String?       = null,
    @SerializedName("note"                  ) var note                  : String?       = null,
    @SerializedName("token"                 ) var token                 : String?       = null,
    @SerializedName("canReset"              ) var canReset              : Boolean?      = null,
    @SerializedName("country"               ) var country               : Country?      = Country(),
    @SerializedName("subscription"          ) var subscription          : Subscription? = Subscription(),
    @SerializedName("accountInfo"           ) var accountInfo           : AccountInfo?  = AccountInfo(),
    @SerializedName("mainBranch"            ) var mainBranch            : MainBranch?   = MainBranch()

)