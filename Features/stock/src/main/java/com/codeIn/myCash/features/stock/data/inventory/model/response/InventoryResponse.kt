package com.codeIn.stock.inventory.data.model.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class InventoryResponse {
    data class Success(val data : InventoryDTO?) : InventoryResponse()
    data class ServerError(val error : ErrorEntity) : InventoryResponse()
    data class ResponseError(val message : String?) : InventoryResponse()
    data object Idle : InventoryResponse()
    data object Loading : InventoryResponse()
    data class InputError(val inputError : InvalidInput) : InventoryResponse()
}