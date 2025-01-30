package com.codeIn.myCash.features.sales.data.expenses.remote.response

import com.google.gson.annotations.SerializedName

data class DetailsExpenseDTO(
    @SerializedName("data")
    val `data`: ExpenseModel?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)