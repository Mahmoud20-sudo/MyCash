package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository){
    suspend operator fun invoke(phone : String, countryId : String, password : String) : AuthenticationState {
        return loginRepository.login(phone , countryId , password)
    }
}