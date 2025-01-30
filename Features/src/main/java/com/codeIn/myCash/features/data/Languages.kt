package com.codeIn.myCash.features.data

enum class Languages(val value:String) {
    AR("ar"),
    EN("en");
    companion object {
        infix fun from(value: String): Languages = Languages.values().firstOrNull { it.value == value } ?: EN
    }
}