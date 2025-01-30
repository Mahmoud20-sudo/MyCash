package com.codeIn.myCash.ui.options.users.user_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO
import com.codeIn.myCash.features.user.data.users.model.response.UsersResponse
import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.usecase.UsersDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val mUsersDetailsUseCase: UsersDetailsUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<List<GetUsersDTO.Data.Data>>(emptyList())
    val user = _user.asStateFlow()

    fun getUsersDetails(employeeId: Int) {
        viewModelScope.launch {
            try {
                val response = mUsersDetailsUseCase.invoke(employeeId)
                if (response is UsersState.SuccessRetrieveUsers && response.data?.status == 1) {
                    val userDto = response.data
                    userDto?.data?.let { data ->
                        _user.value = data.data?.mapNotNull { nestedData ->
                            nestedData
                        } ?: emptyList()
                    }
                } else {
                    // Handle unsuccessful response here
                }
            } catch (e: Exception) {
                // Handle exception here
            }
        }
    }
}