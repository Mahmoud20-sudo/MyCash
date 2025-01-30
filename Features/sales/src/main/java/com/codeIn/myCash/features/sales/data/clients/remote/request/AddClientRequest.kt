package com.codeIn.myCash.features.sales.data.clients.remote.request

data class AddClientRequest(
    val name: String,
    val phone: String,
    val type: Int,
    val clientId: Int? = null,
    val taxNo: String? = null,
    val commercialRecordNo: String? = null,
    val email: String? = null,
    val address: String? = null,
    val extra: String? = null,
    val countryId: String? = null,
)