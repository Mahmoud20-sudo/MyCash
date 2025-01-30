package com.codeIn.myCash.features.user.data.shift.remote.reponse

import android.os.Parcelable
import com.codeIn.myCash.features.user.data.authentication.remote.response.Branch
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistics(
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("endTime")
    val endTime: String?,
    @SerializedName("startDay")
    val startDay: String?,
    @SerializedName("startTime")
    val startTime: String?,
    @SerializedName("totalPrice")
    val totalPrice: String?,
) : Parcelable