package com.codeIn.myCash.features.sales.data.expenses.remote.response

import com.google.gson.annotations.SerializedName

data class ExpensesDTO(
    @SerializedName("data")
    val `data`: ExpensesData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)