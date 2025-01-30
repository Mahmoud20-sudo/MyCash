package com.codeIn.myCash.features.stock.domain.category.model

data class CategoryModelRequest (
    var name : String? = null,
    var child : CategoryModelRequest?,
)
