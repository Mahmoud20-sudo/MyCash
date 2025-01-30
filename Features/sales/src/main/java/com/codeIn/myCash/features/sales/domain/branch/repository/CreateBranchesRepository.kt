package com.codeIn.myCash.features.sales.domain.branch.repository

import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState

interface CreateBranchesRepository {
    suspend fun createBranches(name: String,address:String,isMain:Int,employee_id:Int,status:Int,city:String,additionalInfo: String,phone:String?): BranchesState
}