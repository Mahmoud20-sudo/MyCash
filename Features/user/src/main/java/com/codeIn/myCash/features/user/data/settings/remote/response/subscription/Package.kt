package com.codeIn.myCash.features.user.data.settings.remote.response.subscription

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class Package(
    @SerializedName("country_id")
    val countryId: String?,
    @SerializedName("days")
    val days: String?,
    @SerializedName("desc_ar")
    val descAr: String?,
    @SerializedName("desc_en")
    val descEn: String?,
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("discountType")
    val discountType: String?,
    @SerializedName("duration_ar")
    val durationAr: String?,
    @SerializedName("duration_en")
    val durationEn: String?,
    @SerializedName("duration")
    val duration: String?,
    @SerializedName("finalPrice")
    val finalPrice: String?,
    @SerializedName("id")
    val id: Int,
    val status: Int,
    @SerializedName("image")
    val image: String?,
    @SerializedName("isDiscount")
    val isDiscount: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("name_ar")
    val nameAr: String?,
    @SerializedName("name_en")
    val nameEn: String?,
    @SerializedName("offlineOrOnline")
    val offlineOrOnline: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("usersCount")
    val usersCount: String?,
    @SerializedName("features")
    var features: List<Feature>?
) : Parcelable

