package com.codeIn.myCash.features.sales.data.branch.model.response

import com.codeIn.common.domain.ErrorEntity
import com.codeIn.sales.branch.data.model.response.CreateBranchesDTO

sealed class BranchesState {
    data object Idle : BranchesState()
    data object Loading : BranchesState()
    data class SuccessRetrieveBranches(val data: BranchesDTO?) : BranchesState()
    data class SuccessRetrieveSingleBranch(val data: BranchesDTO?) : BranchesState()
    data class SuccessDeleteAllBranches(val data: DeleteDTO?) : BranchesState()
    data class SuccessDeleteSingleBranch(val data: DeleteDTO?) : BranchesState()
    data class SuccessCreateBranch(val data: CreateBranchesDTO?) : BranchesState()
    data class SuccessUpdateBranch(val data: CreateBranchesDTO?) : BranchesState()
    data class ServerError(val error: ErrorEntity) : BranchesState()
    data class StateError(val message: String?) : BranchesState()
    data object UnAuthorized : BranchesState()
}