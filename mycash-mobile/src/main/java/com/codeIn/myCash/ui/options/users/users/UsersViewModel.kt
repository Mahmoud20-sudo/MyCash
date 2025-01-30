package com.codeIn.myCash.ui.options.users.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.myCash.features.user.data.users.model.response.DeleteDTO
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.usecase.DeleteAllEmployeeUseCase
import com.codeIn.myCash.features.user.domain.users.usecase.DeleteUserUseCase
import com.codeIn.myCash.features.user.domain.users.usecase.RefreshUserUseCase
import com.codeIn.myCash.features.user.domain.users.usecase.UsersUseCase
import com.codeIn.myCash.utilities.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class UsersViewModel @Inject constructor(
    private val mUsersUseCase: UsersUseCase,
    private val mDeleteUsersUseCase: DeleteUserUseCase,
    private val mDeleteAllEmployeeUseCase: DeleteAllEmployeeUseCase,
    private val mRefreshUserUseCase: RefreshUserUseCase
) : ViewModel() {


    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }

    private val _getUsersResultState = MutableStateFlow<UsersState>(UsersState.Idle)
    val getUsersResultState: StateFlow<UsersState> = _getUsersResultState

    private val _deleteUsersResultState = MutableStateFlow<UsersState>(UsersState.Idle)
    val deleteUsersResultState: StateFlow<UsersState> = _deleteUsersResultState

    private val _usersList = MutableStateFlow<List<GetUsersDTO.Data.Data>>(emptyList())
    val usersList: StateFlow<List<GetUsersDTO.Data.Data>> = _usersList

    private val _deleteUser = MutableStateFlow<GetUsersDTO.Data.Data?>(null)
    val deleteUser: StateFlow<GetUsersDTO.Data.Data?> = _deleteUser

    private var _searchText = MutableLiveData("")
    val searchText: LiveData<String?> = _searchText

    /**
     * Called to delete all the users from the list of users.
     * @return [Unit]
     */
    fun deleteUser(user : GetUsersDTO.Data.Data?) = launchIO {
        _deleteUsersResultState.emit(UsersState.Loading)
            try {
                val response = mDeleteUsersUseCase.invoke(user?.id ?: 0)
                _deleteUsersResultState.emit(response)
                if (response is UsersState.SuccessDeleteSingleUser) {
                    _deleteUser.value =user
                } else {
                    // Handle unsuccessful response here
                }
            } catch (e: Exception) {
                // Handle error
        }
    }

    fun deleteAll(all: Int? = null) = launchIO {
        try {
            val response = mDeleteAllEmployeeUseCase.invoke(1)
            _deleteUsersResultState.emit(response)
        } catch (e: Exception) {
            // Handle error
        }
    }

    fun applyFilter(filter: UsersFilterDialog.Filter) {
        // TODO: implement the filter logic
    }

    fun getRefresh() = launchIO {
        _getUsersResultState.emit(UsersState.Loading)
        try {
            val response = if(searchText.value.isNullOrEmpty()) mRefreshUserUseCase.invoke() else mUsersUseCase.invoke(searchText.value)
            _getUsersResultState.emit(response)
            if (response is UsersState.SuccessRetrieveUsers) {
                val userDto = response.data
                userDto?.data?.let { data ->
                    _usersList.value = data.data?.mapNotNull { nestedData ->
                        nestedData
                    } ?: emptyList()
                }
            }
            _getUsersResultState.emit(UsersState.Idle)
        } catch (e: Exception) {
            // Handle exception here
        }
    }

    fun clearState() = launchIO {
        if(_deleteUsersResultState.value !is UsersState.Idle)
            _deleteUsersResultState.emit(UsersState.Idle)
    }

    fun updateSearchText(query: String) {
        _searchText.postValue(query)
    }
}