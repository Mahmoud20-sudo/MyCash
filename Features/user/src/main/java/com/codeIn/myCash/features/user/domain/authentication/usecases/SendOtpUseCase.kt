package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.ForgetPasswordRepository
import javax.inject.Inject

class SendOtpUseCase  @Inject constructor(private val forgetPasswordRepository: ForgetPasswordRepository){
    suspend operator fun invoke(key : String, type : String, countryId : String) : AuthenticationState {
        return forgetPasswordRepository.sendOtp(key , type , countryId)
    }
}