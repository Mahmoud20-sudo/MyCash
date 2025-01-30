package com.codeIn.common.domain.usecases

import javax.inject.Inject

class ValidateEmptyUseCase @Inject constructor(){
    operator fun invoke(data : String?): Boolean{
        return data.isNullOrBlank()
    }
}