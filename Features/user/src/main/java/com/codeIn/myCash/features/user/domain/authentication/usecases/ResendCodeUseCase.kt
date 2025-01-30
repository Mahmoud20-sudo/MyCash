package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.ResendCodeRepository
import javax.inject.Inject

class ResendCodeUseCase@Inject constructor(private val resendCodeRepository: ResendCodeRepository){
    suspend operator fun invoke(phone : String? , countryId : String? ,email : String? , type : String?) : AuthenticationState {
        return resendCodeRepository.resendCode(phone , countryId , email = email , type)
    }
}