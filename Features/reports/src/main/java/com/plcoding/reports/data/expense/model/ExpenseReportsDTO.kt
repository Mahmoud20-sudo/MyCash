package com.plcoding.reports.data.expense.model


import com.google.gson.annotations.SerializedName

data class ExpenseReportsDTO(
    @SerializedName("data")
    val expensesData: ExpensesData? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)