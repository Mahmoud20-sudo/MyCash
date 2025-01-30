package com.codeIn.myCash.features.stock.domain.category.model

data class CategoryModel (
    var id : Int = 0 ,
    var name : String? =null,
    var child : CategoryModel? = null ,
    var addChild : Boolean = false,
    var isFirst : Boolean = false,
    var isEnabled : Boolean = false
)