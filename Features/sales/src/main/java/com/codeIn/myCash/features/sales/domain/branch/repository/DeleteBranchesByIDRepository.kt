package com.codeIn.myCash.features.sales.domain.branch.repository

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState

interface DeleteBranchesByIDRepository {
    suspend fun deleteBranchesByID( branchID: Int? = null): BranchesState
}