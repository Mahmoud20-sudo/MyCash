package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class UserDate(
    @SerializedName("accountInfo")
    val accountInfo: AccountInfo,
    @SerializedName("branch")
    val branch: Any,
    @SerializedName("canReset")
    val canReset: Boolean,
    @SerializedName("country")
    val country: Country,
    @SerializedName("email")
    val email: String,
    @SerializedName("firebase")
    val firebase: Any,
    @SerializedName("has_permission")
    val hasPermission: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invoicesCount")
    val invoicesCount: Int,
    @SerializedName("isCompleteAccountInfo")
    val isCompleteAccountInfo: Int,
    @SerializedName("isCompleteShitInfo")
    val isCompleteShitInfo: Int,
    @SerializedName("lang")
    val lang: String,
    @SerializedName("lastShift")
    val lastShift: Any,
    @SerializedName("mainBranch")
    val mainBranch: MainBranch,
    @SerializedName("msgCode")
    val msgCode: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("note")
    val note: Any,
    @SerializedName("paymentStatus")
    val paymentStatus: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("subscription")
    val subscription: Subscription,
    @SerializedName("token")
    val token: Any,
    @SerializedName("type")
    val type: Int
)