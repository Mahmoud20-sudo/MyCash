package com.codeIn.myCash.features.user.domain.users.usecase

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.repository.DeleteUserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val repository: DeleteUserRepository){
    suspend operator fun invoke(employeeId: Int) : UsersState {
        return repository.deleteUser(employeeId)
    }
}