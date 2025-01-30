package com.codeIn.myCash.features.stock.data.category.remote.response.category

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel

sealed class FilterCategoryState{
    data object Idle : FilterCategoryState()

    data object Loading : FilterCategoryState()

    data class SuccessFilterCategory(val data: CategoriesData?) : FilterCategoryState()

    data class SuccessAddProduct(val data : ProductModel?) : FilterCategoryState()

    data class ServerError(val error : ErrorEntity) : FilterCategoryState()

    data class StateError(val message : String?) : FilterCategoryState()

    data class InputError(val inputError : InvalidInput) : FilterCategoryState()
    data object UnAuthorized : FilterCategoryState()
}
