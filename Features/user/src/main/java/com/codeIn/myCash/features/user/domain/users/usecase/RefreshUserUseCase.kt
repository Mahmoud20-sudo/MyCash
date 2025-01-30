package com.codeIn.myCash.features.user.domain.users.usecase

import com.codeIn.myCash.features.user.data.users.model.response.UsersState
import com.codeIn.myCash.features.user.domain.users.repository.RefreshRepository
import javax.inject.Inject

class RefreshUserUseCase @Inject constructor(private val repository: RefreshRepository){
    suspend operator fun invoke(): UsersState {
        return repository.refreshUser()
    }
}