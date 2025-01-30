package com.codeIn.myCash.features.user.domain.users.usecase

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.repository.UserDetailsRepository
import javax.inject.Inject

class UsersDetailsUseCase @Inject constructor(private val repository: UserDetailsRepository){
    suspend fun invoke(employeeId: Int): UsersState {
        return repository.userDetails(employeeId)
    }
}