package com.codeIn.myCash.features.user.data.settings.remote.response.device

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Device(
    @SerializedName("brand")
    val brand: Brand?,
    @SerializedName("description_ar")
    val descriptionAr: String?,
    @SerializedName("description_en")
    val descriptionEn: String?,
    @SerializedName("deviceFeatures")
    val deviceFeatures: List<DeviceFeature>?,
    @SerializedName("deviceFont")
    val deviceFont: List<DeviceFont>?,
    @SerializedName("devicePaper")
    val devicePaper: List<DevicePaper>?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("name_ar")
    val nameAr: String?,
    @SerializedName("name_en")
    val nameEn: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("device")
    val device: Device?
) : Parcelable
