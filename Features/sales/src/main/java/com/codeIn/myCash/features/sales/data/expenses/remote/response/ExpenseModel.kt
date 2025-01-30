package com.codeIn.myCash.features.sales.data.expenses.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExpenseModel(
    @SerializedName("ExpenseFile")
    var expenseFile: String?,
    @SerializedName("amount")
    var amount: String?,
    @SerializedName("date")
    var date: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("note")
    var note: String?,
    @SerializedName("statement")
    var statement: String? ,
    @SerializedName("additional_info")
    var additionalInfo: String?,
    @SerializedName("created_at")
    var createdAt: String?  ,
    @SerializedName("tax")
    var tax: String? ,
    @SerializedName("totalAmount")
    var totalAmount: String?
) : Parcelable