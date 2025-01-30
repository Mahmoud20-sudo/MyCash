package com.plcoding.reports.data.expense.model


import com.google.gson.annotations.SerializedName

data class ExpensesData(
    @SerializedName("expenses")
    val expenses: Expenses,
    @SerializedName("report")
    val expenseReport: ExpenseReport
)