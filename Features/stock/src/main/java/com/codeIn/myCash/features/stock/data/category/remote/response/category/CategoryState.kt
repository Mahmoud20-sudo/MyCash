package com.codeIn.myCash.features.stock.data.category.remote.response.category

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel

sealed class CategoryState{
    data object Idle : CategoryState()
    data object Loading : CategoryState()
    data class SuccessMainCategories(val data : CategoriesData?) : CategoryState()
    data class SuccessDetailsProduct(val data : ProductModel?) : CategoryState()
    data class SuccessUpdateProduct(val data : ProductModel?) : CategoryState()
    data class ServerError(val error : ErrorEntity) : CategoryState()
    data class StateError(val message : String?) : CategoryState()
    data class InputError(val inputError : InvalidInput) : CategoryState()
    data object UnAuthorized : CategoryState()
}