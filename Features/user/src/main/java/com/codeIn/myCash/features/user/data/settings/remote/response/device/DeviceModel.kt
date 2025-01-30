package com.codeIn.myCash.features.user.data.settings.remote.response.device

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue
import kotlinx.parcelize.Parcelize
@Parcelize
data class DeviceModel(
    @SerializedName("device")
    val device: @RawValue Device?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("price")
    val price: String? ,
    @SerializedName("finalPrice")
    val finalPrice: String? ,
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("isDiscount")
    val isDiscount: String?,
    @SerializedName("discountType")
    val discountType: String?,
    var isSelected : Boolean = false
) : Parcelable
