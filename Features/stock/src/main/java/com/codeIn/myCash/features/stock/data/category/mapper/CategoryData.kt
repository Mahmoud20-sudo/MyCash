package com.codeIn.myCash.features.stock.data.category.mapper

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData
import com.codeIn.common.domain.model.AutoCompleteModelSearch

fun CategoryData.toCategoryModel(): AutoCompleteModelSearch {
    return AutoCompleteModelSearch(
        id = id ,
        name = name
    )
}

fun convertCategory(data : List<CategoryData>?): List<AutoCompleteModelSearch>{
    val list = ArrayList<AutoCompleteModelSearch>()
    for (i in data!!)
    {
        val item = i.toCategoryModel()
        list.add(item)
    }
    return list
}

fun addAllItemToCategoryList(data: List<CategoryData>? , name : String , hasCategory : Boolean = false ) : List<CategoryData>{
    val categoryData = CategoryData(
        id = 0 ,
        name = name ,
        selected = true
    )
    if (hasCategory)
        categoryData.selected = false
    val list =  mutableListOf(categoryData)
    list += data as ArrayList<CategoryData>
    return list
}