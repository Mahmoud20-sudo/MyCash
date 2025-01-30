package com.codeIn.myCash.features.stock.domain.category.model

data class CategoryRequest (
    var categoryId : String? = "" ,
    var parentCategoryId : String? = "",
    var category : String? = ""
)