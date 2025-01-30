package com.codeIn.myCash.features.sales.domain.branch.usecase

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.repository.DeleteBranchesRepository
import javax.inject.Inject

class DeleteBranchesUseCase  @Inject constructor(private val repository : DeleteBranchesRepository){
    suspend operator fun  invoke( all :Int ): BranchesState {
        return repository.deleteBranches(all)
    }
}