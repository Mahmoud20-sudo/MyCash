package com.codeIn.myCash.features.stock.domain.category.usecases

import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import javax.inject.Inject

class DeleteSomeCategoriesUSeCase @Inject constructor() {

    suspend operator fun invoke(position : Int, currentCategories : ArrayList<CategoryModel>?) : ArrayList<CategoryModel>?{
        return if (position != 0) {
            val subList  = currentCategories?.subList(0, position)
            val temp = subList?.toList()?.toTypedArray()?.toCollection(ArrayList<CategoryModel>())
            if (temp?.size!! >= 1)
                temp[temp.size - 1].addChild = false
            temp
        } else {
            val temp = ArrayList<CategoryModel>()
            if (currentCategories?.isNotEmpty() == true)
            {
                temp.add(currentCategories[0])
                temp[0].addChild = false
            }
            temp
        }
    }
}