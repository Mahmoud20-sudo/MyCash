package com.codeIn.myCash.features.user.data.settings.remote.response.subscription

import android.os.Parcelable
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subscription(
    @SerializedName("daysLeft")
    val daysLeft: String?,
    @SerializedName("devicePrice")
    val devicePrice: String?,
    @SerializedName("deviceToken")
    val deviceToken: String?,
    @SerializedName("device")
    val device: DeviceModel?,
    @SerializedName("discountPrice")
    val discountPrice: String?,
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("remaining_days")
    val remaining: String?,
    @SerializedName("expire")
    val expire: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("influencer")
    val influence: String?,
    @SerializedName("package")
    val `package`: Package?,
    @SerializedName("packagePrice")
    val packagePrice: String?,
    @SerializedName("startDate")
    val startDate: String?,
    @SerializedName("taxPrice")
    val taxPrice: String?,
    @SerializedName("totalPrice")
    val totalPrice: String?
) : Parcelable
