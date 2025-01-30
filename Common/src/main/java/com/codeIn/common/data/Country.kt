package com.codeIn.common.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Country(
    @SerializedName("countryCode")
    val countryCode: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("currency_ar")
    val currencyAr: String?,
    @SerializedName("currency_en")
    val currencyEn: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumbers")
    val phoneNumbers: Int,
    @SerializedName("status")
    val status: Int
) : Parcelable
