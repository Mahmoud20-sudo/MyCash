package com.plcoding.reports.data.expense.model


import com.google.gson.annotations.SerializedName

data class ExpenseModel(
    @SerializedName("additional_info")
    val additionalInfo: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("ExpenseFile")
    val expenseFile: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("note")
    val note: String,
    @SerializedName("statement")
    val statement: String,
    @SerializedName("tax")
    val tax: Int,
    @SerializedName("taxPrice")
    val taxPrice: String,
    @SerializedName("totalAmount")
    val totalAmount: String
)