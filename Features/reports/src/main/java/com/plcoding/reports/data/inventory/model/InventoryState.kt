package com.plcoding.reports.data.inventory.model

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class InventoryState{
    data object Idle : InventoryState()

    data object Loading : InventoryState()

    data class Success(val data : Data?) : InventoryState()
    data class SuccessShowReports(val data : Data?) : InventoryState()
    data class SuccessDeleteReports(val message: String?) : InventoryState()
    data class ServerError(val error : ErrorEntity) : InventoryState()
    data class SuccessDetailsReports(val data : Data?) : InventoryState()

    data class StateError(val message : String?) : InventoryState()

    data class InputError(val inputError : InvalidInput) : InventoryState()
    data object UnAuthorized : InventoryState()
}