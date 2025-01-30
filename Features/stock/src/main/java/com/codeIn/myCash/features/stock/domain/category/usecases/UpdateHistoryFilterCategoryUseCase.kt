package com.codeIn.myCash.features.stock.domain.category.usecases

import com.codeIn.myCash.features.stock.domain.category.model.HistoryCategoryModel
import com.codeIn.myCash.features.stock.domain.category.repository.CategoryRepository
import javax.inject.Inject

class UpdateHistoryFilterCategoryUseCase @Inject constructor(private val repository: CategoryRepository){
    suspend operator fun invoke(historyCategoryModel: HistoryCategoryModel ) : Boolean {
        return repository.updateHistoryFilterCategory(historyCategoryModel)
    }
}