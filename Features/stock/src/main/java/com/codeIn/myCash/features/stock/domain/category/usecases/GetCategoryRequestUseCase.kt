package com.codeIn.myCash.features.stock.domain.category.usecases

import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import com.codeIn.myCash.features.stock.domain.category.model.CategoryRequest
import com.codeIn.myCash.features.stock.domain.category.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryRequestUseCase @Inject constructor(private val repository: CategoryRepository){
    suspend operator fun invoke(currentCategoryModel : CategoryModel?, selectedCategories : ArrayList<CategoryModel>?) : CategoryRequest? {
        return repository.getCategoryRequest(currentCategoryModel , selectedCategories)
    }
}