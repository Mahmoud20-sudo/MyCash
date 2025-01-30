package com.codeIn.myCash.features.sales.domain.branch.usecase

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.repository.DeleteBranchesByIDRepository
import retrofit2.Response
import javax.inject.Inject

class DeleteBranchIDUSeCase @Inject constructor(private val repository : DeleteBranchesByIDRepository){
    suspend operator fun  invoke( branchID :Int ): BranchesState {
        return repository.deleteBranchesByID(branchID)
    }
}