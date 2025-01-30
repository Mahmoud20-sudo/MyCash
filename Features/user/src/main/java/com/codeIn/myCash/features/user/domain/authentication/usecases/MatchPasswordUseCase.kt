package com.codeIn.myCash.features.user.domain.authentication.usecases

import javax.inject.Inject

class MatchPasswordUseCase @Inject constructor(){
    operator fun invoke(password : String? , confirmPassword : String?): Boolean{
        return password == confirmPassword
    }
}