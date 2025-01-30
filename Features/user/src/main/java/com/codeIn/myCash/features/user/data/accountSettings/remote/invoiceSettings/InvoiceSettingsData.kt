package com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings
import android.os.Parcelable
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AccountInfo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceSettingsData(
    @SerializedName("commercialRecord")
    val commercialRecord: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("active")
    val active: Int,
    @SerializedName("simpleInvoice")
    val simpleInvoice: InvoiceSettings?,
    @SerializedName("tax")
    val tax: String?,
    @SerializedName("taxInvoice")
    val taxInvoice: InvoiceSettings?,
    @SerializedName("taxRecord")
    val taxRecord: String?,
    @SerializedName("accountInfo")
    val accountInfo: AccountInfo?
) : Parcelable
