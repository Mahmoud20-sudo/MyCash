package com.codeIn.common.domain.usecases

import android.util.Patterns
import javax.inject.Inject

class ValidatePhoneSaudiUseCase @Inject constructor() {
    operator fun invoke(phoneNumber : String?): Boolean{
       val validate = true /*if (phoneNumber?.isNotEmpty() == true)
            phoneNumber[0] =='5'
        else
            false*/
        return Patterns.PHONE.matcher(phoneNumber!!).matches() && phoneNumber.length == 9 && validate
    }
}