package com.codeIn.myCash.features.sales.domain.branch.repository

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState

interface UpdateBranchRepository {
    suspend fun updateBranch(name:String,isMain: Int, address: String,branchID:Int,status: Int,additionalInfo: String,city:String,phone:String?): BranchesState
}