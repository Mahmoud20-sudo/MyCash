package com.codeIn.myCash.features.sales.data.clients.remote.response

import android.os.Parcelable
import com.codeIn.common.data.Country
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClientModel(
    @SerializedName("address")
    val address: String?,
    @SerializedName("commercialRecord")
    val commercialRecord: String?,
    @SerializedName("country")
    val country: Country? = null,
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("taxRecord")
    val taxRecord: String?,
    @SerializedName("totalUnPaid")
    val totalUnPaid : String?,
) : Parcelable