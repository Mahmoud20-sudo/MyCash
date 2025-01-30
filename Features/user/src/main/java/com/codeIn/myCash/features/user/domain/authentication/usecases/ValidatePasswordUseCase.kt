package com.codeIn.myCash.features.user.domain.authentication.usecases

import java.util.regex.Pattern
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password : String?) : Boolean{
        val passwordPattern : Pattern =
            Pattern.compile("^" + // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 6 characters
                    "$")

        return passwordPattern.matcher(password).matches()
    }
}