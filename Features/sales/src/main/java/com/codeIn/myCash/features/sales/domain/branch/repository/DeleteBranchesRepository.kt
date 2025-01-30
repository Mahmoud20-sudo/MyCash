package com.codeIn.myCash.features.sales.domain.branch.repository

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState

interface DeleteBranchesRepository {
    suspend fun deleteBranches( all: Int? = null): BranchesState
}