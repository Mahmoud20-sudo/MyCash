package com.codeIn.myCash.features.stock.domain.category.model

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData

data class HistoryCategoryModel (
    var parentId : String? = null ,
    var parentName : String? = null,
    var list: List<CategoryData>? = null
)