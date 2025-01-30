package com.codeIn.myCash.features.sales.domain.branch.usecase

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.repository.BranchDetailsRepository
import javax.inject.Inject

class BranchDetailsUseCase @Inject constructor(private val repository : BranchDetailsRepository) {
    suspend operator fun invoke(branchID: Int): BranchesState {
        return repository.getBranchDetails(branchID)
    }
}