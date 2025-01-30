package com.codeIn.myCash.features.sales.domain.branch.usecase

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.repository.UpdateBranchRepository
import javax.inject.Inject

class UpdateBranchUseCase @Inject constructor(private val repository: UpdateBranchRepository){
    suspend operator fun invoke(name:String,isMain: Int, address: String,branchID:Int,status: Int,additionalInfo: String,city:String,phone:String?) : BranchesState {
        return repository.updateBranch(name, isMain, address, branchID, status,additionalInfo, city, phone)
    }
}