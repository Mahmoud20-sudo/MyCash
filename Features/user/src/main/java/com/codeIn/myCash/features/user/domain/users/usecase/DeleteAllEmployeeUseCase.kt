package com.codeIn.myCash.features.user.domain.users.usecase

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.repository.DeleteAllEmployeeRepository
import javax.inject.Inject

class DeleteAllEmployeeUseCase @Inject constructor(private val repository: DeleteAllEmployeeRepository){
    suspend operator fun invoke(deleteAll :Int) : UsersState {
        return repository.deleteAllEmployee(deleteAll)
    }
}