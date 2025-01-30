package com.plcoding.reports.data.expense.model


import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class Expenses(
    @SerializedName("data")
    val `data`: List<ExpenseModel>,
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("text")
    val text: String,
    @SerializedName("total_expenses")
    val totalExpenses: Double
)