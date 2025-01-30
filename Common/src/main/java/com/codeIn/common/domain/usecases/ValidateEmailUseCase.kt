package com.codeIn.common.domain.usecases

import android.util.Patterns
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(){
    operator fun invoke(email : String?): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }
}