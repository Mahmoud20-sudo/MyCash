package com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceSettings(
    @SerializedName("active")
    val active: Int,
    @SerializedName("cashier")
    var cashier: String?,
    @SerializedName("client")
    var client: String?,
    @SerializedName("footerText")
    var footerText: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("myCashQr")
    var myCashQr: String?,
    @SerializedName("productDesc")
    var productDesc: String?,
    @SerializedName("zatcaQr")
    var zatcaQr: String?
) : Parcelable