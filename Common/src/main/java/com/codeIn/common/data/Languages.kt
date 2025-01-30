package com.codeIn.common.data

enum class Languages(val value:String) {
    AR("ar"),
    EN("en");
    companion object {
        infix fun from(value: String): Languages = entries.firstOrNull { it.value == value } ?: EN
    }
}

enum class PaymentMethods(val value:String) {
    CARD("card"),
    CASH("cash"),
    CASH_CARD("cash_card");
    companion object {
        infix fun from(value: String): PaymentMethods = entries.firstOrNull { it.value == value } ?: CASH
    }
}