package com.codeIn.myCash.features.user.domain.authentication.usecases

import android.util.Patterns
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class ValidatePhoneEgyptUseCase  @Inject constructor(private val validateNotEmptyUseCase: ValidateEmptyUseCase) {
    operator fun invoke(phoneNumber : String?): Boolean{
        return if (validateNotEmptyUseCase.invoke(phoneNumber))
            Patterns.PHONE.matcher(phoneNumber!!).matches() && phoneNumber.length == 11
        else
            false
    }
}