package com.codeIn.myCash.features.user.data.authentication.remote.response.user

import android.os.Parcelable
import com.codeIn.myCash.features.user.data.authentication.remote.response.Branch
import com.codeIn.common.data.Country
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Subscription
import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftData
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    @SerializedName("accountInfo")
    val accountInfo: AccountInfo?,
    @SerializedName("canReset")
    val canReset: Boolean,
    @SerializedName("country")
    val country: Country,
    @SerializedName("email")
    val email: String?,
    @SerializedName("firebase")
    val firebase: String?,
    @SerializedName("has_permission")
    val hasPermission: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invoicesCount")
    val invoicesCount: String?,
    @SerializedName("isCompleteAccountInfo")
    val isCompleteAccountInfo: String?,
    @SerializedName("isCompleteShitInfo")
    val isCompleteShitInfo: String?,
    @SerializedName("lang")
    val lang: String?,
    @SerializedName("mainBranch")
    val mainBranch: Branch?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("paymentStatus")
    val paymentStatus: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("subscription")
    val subscription: Subscription?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("msgCode")
    val msgCode: String?,
    @SerializedName("lastShift")
    val lastShift : ShiftData? ,
) : Parcelable