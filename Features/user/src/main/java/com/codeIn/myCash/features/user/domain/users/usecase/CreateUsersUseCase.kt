package com.codeIn.myCash.features.user.domain.users.usecase

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.repository.CreateUsersRepository
import javax.inject.Inject

class CreateUsersUseCase @Inject constructor(private val repository: CreateUsersRepository){
    suspend operator fun invoke( name: String, email: String, phone: String?, password: String,
                                 status: Int , branchId : String? , type : String?):UsersState{
        return repository.createUser(name, email, phone, password, status, branchId , type )
    }
}