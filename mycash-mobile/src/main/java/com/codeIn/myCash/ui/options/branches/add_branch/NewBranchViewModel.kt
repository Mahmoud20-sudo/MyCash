package com.codeIn.myCash.ui.options.branches.add_branch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesResponse
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.usecase.BranchesUseCase
import com.codeIn.myCash.features.sales.domain.branch.usecase.CreateBranchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewBranchViewModel @Inject constructor(
    private val createBranchesUseCase : CreateBranchesUseCase,
    ) : ViewModel() {
    private val _branchesResponse = MutableStateFlow<BranchesState>(BranchesState.Idle)
    val branchesResponse = _branchesResponse.asStateFlow()

    fun createBranch(name: String, address: String, isMain: Int, employeeId: Int, status: Int,city:String,additionalInfo: String,phone:String?) =
        launchIO {
            _branchesResponse.emit(BranchesState.Loading)
            createBranchesUseCase.invoke(name,address,isMain,employeeId,status,city, additionalInfo, phone)
                .let {
                    _branchesResponse.emit(it)
                }
        }

}