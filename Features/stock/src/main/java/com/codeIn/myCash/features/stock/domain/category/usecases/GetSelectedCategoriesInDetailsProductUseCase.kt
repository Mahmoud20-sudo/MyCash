package com.codeIn.myCash.features.stock.domain.category.usecases

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData
import com.codeIn.myCash.features.stock.data.product.remote.response.SelectedCategory
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import com.codeIn.myCash.features.stock.domain.category.repository.CategoryRepository
import javax.inject.Inject

class GetSelectedCategoriesInDetailsProductUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(data : CategoryData) :  ArrayList<CategoryModel>? {
        return categoryRepository.getSelectedCategoryModel(data)
    }
}