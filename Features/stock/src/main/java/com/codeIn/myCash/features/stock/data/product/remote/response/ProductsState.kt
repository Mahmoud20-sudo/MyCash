package com.codeIn.myCash.features.stock.data.product.remote.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState

sealed class ProductsState{
    data object Idle : ProductsState()

    data object Loading : ProductsState()

    data class Success(val data : ProductsData?) : ProductsState()
    data class SuccessShowProducts(val data : ProductsData?) : ProductsState()
    data class SuccessDeleteProduct(val message: String?) : ProductsState()
    data class ServerError(val error : ErrorEntity) : ProductsState()
    data class SuccessDetailsProduct(val data : ProductModel?) : ProductsState()

    data class StateError(val message : String?) : ProductsState()

    data class InputError(val inputError : InvalidInput) : ProductsState()
    data object UnAuthorized : ProductsState()
}