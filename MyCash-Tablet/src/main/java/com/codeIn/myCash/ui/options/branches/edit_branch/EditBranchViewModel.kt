package com.codeIn.myCash.ui.options.branches.edit_branch

import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesResponse
import com.codeIn.myCash.features.sales.domain.branch.usecase.UpdateBranchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EditBranchViewModel @Inject constructor(private val updateBranchUseCase: UpdateBranchUseCase):ViewModel(){
    private val _updateBranchResponse = MutableStateFlow<BranchesResponse>(BranchesResponse.Idle)
    val updateBranchResponse = _updateBranchResponse.asStateFlow()

    fun updateBranch( name: String, isMain: Int, address: String, branchID: Int, status: Int,additionalInfo: String,city:String,phone:String?){
        launchIO {
            _updateBranchResponse.emit(BranchesResponse.Loading)
            updateBranchUseCase.invoke(name, isMain, address, branchID, status,additionalInfo, city, phone)
                .let { _updateBranchResponse }
        }
    }
}