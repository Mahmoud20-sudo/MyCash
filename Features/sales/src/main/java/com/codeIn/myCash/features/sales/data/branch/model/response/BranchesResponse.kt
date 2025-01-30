package com.codeIn.myCash.features.sales.data.branch.model.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.sales.branch.data.model.response.CreateBranchesDTO

sealed class BranchesResponse {
    data class Success(val data : CreateBranchesDTO?) : BranchesResponse()
    data class ServerError(val error : ErrorEntity) : BranchesResponse()
    data class ResponseError(val message : String?) : BranchesResponse()
    data object Idle : BranchesResponse()
    data object Loading : BranchesResponse()
    data class InputError(val inputError : InvalidInput) : BranchesResponse()
}