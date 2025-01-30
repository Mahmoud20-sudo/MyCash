package com.codeIn.myCash.features.user.data.shift.remote.reponse

import android.os.Parcelable
import com.codeIn.myCash.features.user.data.authentication.remote.response.Branch
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShiftData(
    @SerializedName("currentCash")
    val currentCash: String?,
    @SerializedName("currentVisa")
    val currentVisa: String?,
    @SerializedName("endCash")
    val endCash: String?,
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("endVisa")
    val endVisa: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("mainBranch")
    val mainBranch: Branch?,
    @SerializedName("startCash")
    val startCash: String?,
    @SerializedName("startDate")
    val startDate: String?,
    @SerializedName("startVisa")
    val startVisa: String?,
    @SerializedName("statistics")
    val statistics: Statistics?,
    @SerializedName("branch")
    val branch: Branch?
) : Parcelable
