package com.codeIn.myCash.features.user.domain.users.usecase

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.repository.UpdateUsersRepository
import javax.inject.Inject

class UpdateUsersUseCase @Inject constructor(private val repository: UpdateUsersRepository) {
    suspend operator fun invoke(
        name: String,
        email: String,
        phone: String?,
        password: String,
        note: String,
        employeeId: Int ,
        branchId : String? ,
        type : String?
    ): UsersState {
        return repository.updateUsers(name, email, phone, password, note,
            employeeId, branchId , type)
    }
}