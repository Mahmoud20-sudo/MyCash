package com.codeIn.myCash.features.user.domain.users.usecase

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.repository.UsersRepository
import javax.inject.Inject

class UsersUseCase @Inject constructor(private val repository: UsersRepository){
    suspend operator fun invoke(searchText : String?): UsersState {
        return repository.getUser(searchText)
    }
}