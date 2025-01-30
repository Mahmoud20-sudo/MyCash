package com.codeIn.myCash.ui.options.branches

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Branch (

    @SerializedName("id"                 ) var id                 : Int?                = null,
    @SerializedName("title "             ) var title              : String?             = null,
    @SerializedName("address"            ) var address            : String?             = null,
    @SerializedName("is_main"            ) var isMain             : Boolean?            = null,
    @SerializedName("is_active"          ) var isActive           : Boolean?            = null,
) : Parcelable