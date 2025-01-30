package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.RegisterRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository){
    suspend operator fun invoke(phone : String, countryId : String,email : String ,  password : String, type: Int) : AuthenticationState {
        return registerRepository.register(phone , countryId , password = password , email = email , type = type)
    }
}