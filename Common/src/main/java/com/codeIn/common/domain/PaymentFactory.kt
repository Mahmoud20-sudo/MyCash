package com.codeIn.common.domain

import android.content.Context

interface PaymentFactory {
    fun payment(context: Context, amount: String, phone: String)

    fun handleFailureCode(context: Context, failureLink: String): String?
}