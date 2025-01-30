package com.codeIn.myCash.features.stock.domain.category.repository

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.category.remote.response.category.FilterCategoryState
import com.codeIn.myCash.features.stock.data.product.remote.response.SelectedCategory
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import com.codeIn.myCash.features.stock.domain.category.model.CategoryRequest
import com.codeIn.myCash.features.stock.domain.category.model.HistoryCategoryModel

interface CategoryRepository {

    suspend fun getCategories(parentOnly : String? , parentId : String?): CategoryState

    suspend fun getFilterCategories(parentOnly: String?, parentId: String?, categoryId : String?): FilterCategoryState

    suspend fun updateHistoryFilterCategory(historyCategoryModel: HistoryCategoryModel) : Boolean

    suspend fun getHistoryFilterCategory() : List<CategoryData>?

    suspend fun getCategoryRequest(currentCategoryModel : CategoryModel? ,selectedCategories : List<CategoryModel>?): CategoryRequest?

    suspend fun getSelectedCategoryModel(data : CategoryData): ArrayList<CategoryModel>?
}