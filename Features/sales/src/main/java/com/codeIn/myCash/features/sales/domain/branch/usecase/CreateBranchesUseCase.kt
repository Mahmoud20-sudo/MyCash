package com.codeIn.myCash.features.sales.domain.branch.usecase

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.repository.CreateBranchesRepository
import javax.inject.Inject

class CreateBranchesUseCase @Inject constructor(private val repository: CreateBranchesRepository){
    suspend operator fun invoke(name: String,address:String,isMain:Int,employee_id:Int,status:Int,city:String,additionalInfo: String,phone:String?): BranchesState {
        return repository.createBranches(name,address, isMain, employee_id, status,city, additionalInfo, phone)
    }
}