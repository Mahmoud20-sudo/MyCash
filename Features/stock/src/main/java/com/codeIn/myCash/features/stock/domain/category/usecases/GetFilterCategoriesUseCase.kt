package com.codeIn.myCash.features.stock.domain.category.usecases

import com.codeIn.myCash.features.stock.data.category.remote.response.category.FilterCategoryState
import com.codeIn.myCash.features.stock.domain.category.repository.CategoryRepository
import javax.inject.Inject

class GetFilterCategoriesUseCase @Inject constructor(private val repository: CategoryRepository){
    suspend operator fun invoke(  parentOnly: String?, parentId: String?, categoryId : String? ) : FilterCategoryState {
        return repository.getFilterCategories(parentOnly, parentId , categoryId)
    }
}