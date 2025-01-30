package com.codeIn.myCash.features.stock.domain.category.usecases

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.domain.category.repository.CategoryRepository
import javax.inject.Inject

class GetMainCategoriesUseCase @Inject constructor(private val repository: CategoryRepository){
    suspend operator fun invoke(parentOnly : String? , parentId : String? ) : CategoryState {
        return repository.getCategories(parentOnly, parentId)
    }
}