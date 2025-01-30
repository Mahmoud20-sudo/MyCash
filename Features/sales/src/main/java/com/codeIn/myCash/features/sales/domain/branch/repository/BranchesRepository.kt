package com.codeIn.myCash.features.sales.domain.branch.repository

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState

interface BranchesRepository {
    suspend fun getBranches(searchText : String?) : BranchesState
}