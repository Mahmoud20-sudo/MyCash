package com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt

import com.google.gson.annotations.SerializedName

data class DiscountCodeData(
    @SerializedName("InfluencerSocial")
    val InfluencerSocial: Any,
    @SerializedName("code")
    val code: String,
    @SerializedName("commission")
    val commission: String,
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("days")
    val days: Int,
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("note")
    val note: Any,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("status")
    val status: Int
)
