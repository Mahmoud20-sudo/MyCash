package com.codeIn.myCash.features.stock.domain.category.model

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryCategoryModule @Inject constructor(){
    val stack =  java.util.ArrayDeque<HistoryCategoryModel>()


    fun setItem(historyCategoryModel : HistoryCategoryModel){
        stack.add(historyCategoryModel)
    }

    fun getItem(): List<CategoryData>?{
        if (!stack.isEmpty())
            return  stack.pop().list
        return null
    }

    fun updateLastItem(list: List<CategoryData>?){
        if (!stack.isEmpty())
            stack.peek()?.list = list
    }
}
