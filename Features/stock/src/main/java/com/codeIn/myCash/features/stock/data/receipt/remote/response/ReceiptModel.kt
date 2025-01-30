package com.codeIn.myCash.features.stock.data.receipt.remote.response

import android.os.Parcelable
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReceiptModel(
    @SerializedName("amount")
    val amount: String?,
    @SerializedName("client")
    val client: ClientModel?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invoiceData")
    val invoiceData: InvoiceModel?,
    @SerializedName("paymentStatus")
    val paymentStatus: String? ,
) : Parcelable