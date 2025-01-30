package com.codeIn.myCash.features.user.domain.authentication.usecases


import javax.inject.Inject

class ValidateNotEmptyUseCase @Inject constructor(){
    operator fun invoke(data : String?): Boolean{
        return !data.isNullOrBlank()
    }
}