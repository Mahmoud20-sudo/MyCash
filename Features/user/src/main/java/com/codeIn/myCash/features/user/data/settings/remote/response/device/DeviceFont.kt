package com.codeIn.myCash.features.user.data.settings.remote.response.device

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceFont(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name_ar")
    val nameAr: String?,
    @SerializedName("name_en")
    val nameEn: String?
) : Parcelable
