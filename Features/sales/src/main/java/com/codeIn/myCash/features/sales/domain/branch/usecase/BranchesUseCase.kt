package com.codeIn.myCash.features.sales.domain.branch.usecase

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.repository.BranchesRepository
import javax.inject.Inject

class BranchesUseCase @Inject constructor(private val repository: BranchesRepository) {
    suspend operator fun invoke(searchText : String? = null): BranchesState {
        return repository.getBranches(searchText)
    }

}