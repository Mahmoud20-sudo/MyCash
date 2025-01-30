package com.codeIn.myCash.integration.utilities

import android.util.Patterns
import java.util.regex.Pattern
import javax.inject.Inject

enum class InvalidInput {
    NONE, EMAIL, PASSWORD, CONFIRM_PASSWORD, PHONE_EGYPT, PHONE_SAUDI, TEXT
}

class Validation @Inject constructor() {

    companion object {
        private val EMAIL_PATTERN = Patterns.EMAIL_ADDRESS
        private val PHONE_EGYPT_PATTERN = Pattern.compile("^01[0-2][0-9]{8}$")
        private val PHONE_SAUDI_PATTERN = Pattern.compile("^5[0-9]{8}$")
        private val PASSWORD_PATTERN = Pattern.compile(
            "^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 4 characters
                    "$"
        )
    }


    fun validate(
        email: String? = null,
        password: String? = null,
        confirmPassword: String? = null,
        phoneNumberEgypt: String? = null,
        phoneNumberSaudi: String? = null,
        text: String? = null
    ): InvalidInput {
        return when {
            email != null && !validateEmail(email) -> InvalidInput.EMAIL
            phoneNumberEgypt != null && !validatePhoneEgypt(phoneNumberEgypt) -> InvalidInput.PHONE_EGYPT
            phoneNumberSaudi != null && !validatePhoneSaudi(phoneNumberSaudi) -> InvalidInput.PHONE_SAUDI
            password != null         && !validatePassword(password)           -> InvalidInput.PASSWORD
            confirmPassword != null  && confirmPassword != password           -> InvalidInput.CONFIRM_PASSWORD
            text != null             && !validateText(text)                   -> InvalidInput.TEXT
            else                                                              -> InvalidInput.NONE
        }
    }

    private fun validateEmail(email: String): Boolean = EMAIL_PATTERN.matcher(email).matches()

    private fun validatePassword(password: String): Boolean =
        PASSWORD_PATTERN.matcher(password).matches()

    private fun validatePhoneEgypt(phoneNumber: String): Boolean =
        PHONE_EGYPT_PATTERN.matcher(phoneNumber).matches()

    private fun validatePhoneSaudi(phoneNumber: String): Boolean =
        PHONE_SAUDI_PATTERN.matcher(phoneNumber).matches()

    private fun validateText(text: String): Boolean = text.isNotBlank()

}