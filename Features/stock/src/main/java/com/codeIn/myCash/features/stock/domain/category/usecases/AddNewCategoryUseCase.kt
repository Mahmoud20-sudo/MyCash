package com.codeIn.myCash.features.stock.domain.category.usecases

import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import javax.inject.Inject

class AddNewCategoryUseCase @Inject constructor() {

    suspend operator fun invoke(newCategory : CategoryModel , currentCategories : ArrayList<CategoryModel>?) : ArrayList<CategoryModel>?{
        currentCategories?.add(newCategory)
        return currentCategories
    }
}