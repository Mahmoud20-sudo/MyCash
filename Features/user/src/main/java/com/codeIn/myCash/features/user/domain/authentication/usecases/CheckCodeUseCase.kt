package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.CheckCodeRepository
import javax.inject.Inject

class CheckCodeUseCase @Inject constructor(private val checkCodeRepository: CheckCodeRepository){
    suspend operator fun invoke(otp : String? , phone : String? , countryId : String? ,
                                type : String , //1=>phone , 2 =>email
                                email : String? , active : String? , key : String?) : AuthenticationState {
        return checkCodeRepository.checkCode(otp, phone , countryId , type , email , active , key )
    }
}