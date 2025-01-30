package com.codeIn.myCash.features.user.data.settings.remote.response.subscription

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Feature(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name_ar")
    val nameAr : String,
    @SerializedName("name_en")
    val nameEn : String,
    @SerializedName("name")
    val name : String,
) : Parcelable
