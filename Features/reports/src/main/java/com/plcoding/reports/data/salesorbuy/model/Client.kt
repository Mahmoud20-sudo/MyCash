package com.plcoding.reports.data.salesorbuy.model


import com.google.gson.annotations.SerializedName


data class Client(
    @SerializedName("address")
    val address: Any,
    @SerializedName("commercialRecord")
    val commercialRecord: Any,
    @SerializedName("country")
    val country: Country,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("notes")
    val notes: Any,
    @SerializedName("payment_status")
    val paymentStatus: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("taxRecord")
    val taxRecord: Any,
    @SerializedName("totalUnPaid")
    val totalUnPaid: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("user_id")
    val userId: Int
)