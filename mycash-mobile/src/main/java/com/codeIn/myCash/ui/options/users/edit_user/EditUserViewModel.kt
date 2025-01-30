package com.codeIn.myCash.ui.options.users.edit_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.usecase.BranchesUseCase
import com.codeIn.myCash.features.user.data.users.model.response.UsersResponse
import com.codeIn.myCash.features.user.domain.users.usecase.UpdateUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val mUpdateUsersUseCase: UpdateUsersUseCase,
    private val branchesUseCase: BranchesUseCase,
) : ViewModel() {
    private val _userResponse = MutableStateFlow<UsersResponse>(UsersResponse.Idle)
    val editUsers = _userResponse.asStateFlow()

    private val _selectedBranch = MutableLiveData<BranchesDTO.Data.Data?>()
    val selectedBranch: LiveData<BranchesDTO.Data.Data?> = _selectedBranch

    private val _branchesList = MutableLiveData<List<BranchesDTO.Data.Data>>()
    val branchesList: LiveData<List<BranchesDTO.Data.Data>> = _branchesList

    private val _getBranchesResultState = MutableStateFlow<BranchesState>(BranchesState.Idle)
    val getBranchesResultState = _getBranchesResultState.asStateFlow()


    init {
        getBranches(null)
    }

    fun editUser(
        name: String, email: String, phone: String?, password: String,
        note: String, employeeId: Int, type: String?, branchId: String?
    ) {
        launchIO {
            _userResponse.emit(UsersResponse.Loading)
            mUpdateUsersUseCase.invoke(
                name, email, phone, password, note, employeeId,
                branchId, type
            ).let {
                    _userResponse
            }
        }
    }

    fun updateSelectedBranch(branch: BranchesDTO.Data.Data?) {
        _selectedBranch.value = branch
    }

    private fun getBranches(search: String? = null) = launchIO {
        _getBranchesResultState.emit(BranchesState.Loading)
        try {
            val response = branchesUseCase.invoke(search)
            _getBranchesResultState.emit(response)
            if (response is BranchesState.SuccessRetrieveBranches) {
                val branchesDTO = response.data
                branchesDTO?.data?.let { data ->
                    _branchesList.value = data.data?.mapNotNull { nestedData ->
                        nestedData
                    } ?: emptyList()
                }
                _getBranchesResultState.emit(BranchesState.Idle)
            } else {
                // Handle unsuccessful response here
            }
        } catch (e: Exception) {
            // Handle exception here
        }
    }
}