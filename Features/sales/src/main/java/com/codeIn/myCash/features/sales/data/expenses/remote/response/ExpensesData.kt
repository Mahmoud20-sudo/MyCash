package com.codeIn.myCash.features.sales.data.expenses.remote.response

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName

data class ExpensesData(
    @SerializedName("data")
    val `data`: List<ExpenseModel>?,
    @SerializedName("pagination")
    val pagination: Pagination?,
    @SerializedName("total_expenses")
    val totalExpenses: String?,
    @SerializedName("text")
    val text: String?
)