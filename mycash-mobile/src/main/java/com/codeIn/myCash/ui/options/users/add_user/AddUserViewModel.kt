package com.codeIn.myCash.ui.options.users.add_user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.common.util.launchIO
import com.codeIn.common.util.launchMain
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.usecase.BranchesUseCase
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.usecase.CreateUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val mCreateUserUseCase: CreateUsersUseCase,
    private val branchesUseCase: BranchesUseCase,
) : ViewModel() {

    private val _userResponse = MutableStateFlow<UsersState>(UsersState.Idle)
    val usersResponse = _userResponse.asStateFlow()

    private val _branchesList = MutableLiveData<List<BranchesDTO.Data.Data>>()
    val branchesList: LiveData<List<BranchesDTO.Data.Data>> = _branchesList

    private val _selectedBranch = MutableLiveData<BranchesDTO.Data.Data?>()
    val selectedBranch: LiveData<BranchesDTO.Data.Data?> = _selectedBranch

    private val _branchesResponse = MutableStateFlow<BranchesState>(BranchesState.Idle)
    val branchesResponse = _branchesResponse.asStateFlow()


    init {
        getBranches()
    }

    fun createUser(
        name: String,
        email: String,
        phone: String?,
        password: String,
        status: Int,
        type: String?
    ) {
        launchMain {
            _userResponse.emit(UsersState.Loading)
            try {
                mCreateUserUseCase.invoke(
                    name,
                    email,
                    phone,
                    password,
                    status,
                    (selectedBranch.value?.id ?: 0).toString(),
                    type
                ).let {
                    _userResponse.emit(it)
                }
            } catch (e: Exception) {
                _userResponse.emit(UsersState.StateError(e.message))
            }
        }
    }

    private fun getBranches(search: String? = null) = launchIO {
        _branchesResponse.emit(BranchesState.Loading)
        try {
            val response = branchesUseCase.invoke(search)
            _branchesResponse.emit(response)
            if (response is BranchesState.SuccessRetrieveBranches) {
                val branchesDTO = response.data
                branchesDTO?.data?.let { data ->
                    _branchesList.value = data.data?.mapNotNull { nestedData ->
                        nestedData
                    } ?: emptyList()
                }
                clearState()
            } else {
                // Handle unsuccessful response here
            }
        } catch (e: Exception) {
            // Handle exception here
        }
    }

    fun updateSelectedBranch(branch: BranchesDTO.Data.Data?) {
        _selectedBranch.value = branch
    }

    fun clearState() = launchIO {
        if (_userResponse.value !is UsersState.Idle)
            _userResponse.emit(UsersState.Idle)
        if(_branchesResponse.value !is BranchesState.Idle)
            _branchesResponse.emit(BranchesState.Idle)
    }
}