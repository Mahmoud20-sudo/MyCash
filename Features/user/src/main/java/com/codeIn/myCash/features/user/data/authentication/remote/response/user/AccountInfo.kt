package com.codeIn.myCash.features.user.data.authentication.remote.response.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountInfo(
    @SerializedName("commercialRecord")
    val commercialRecord: String?,
    @SerializedName("commercialRecordName")
    val commercialRecordName: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("quickInvoice")
    val quickInvoice: String?,
    @SerializedName("tax")
    val tax: String?,
    @SerializedName("taxRecord")
    val taxRecord: String?,
    @SerializedName("notification")
    val notification: String?,
    @SerializedName("drafts")
    val drafts: String?
) : Parcelable
