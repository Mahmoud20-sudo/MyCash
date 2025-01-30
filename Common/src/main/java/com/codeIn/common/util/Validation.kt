package com.codeIn.common.util

import android.util.Patterns
import com.codeIn.common.data.InvalidInput
import java.util.regex.Pattern
import javax.inject.Inject



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
        private val COMMERCIAL_RECORD_PATTERN = Pattern.compile("[0-9]{10}$")
        private val TAX_RECORD_PATTERN = Pattern.compile("[0-9]{15}$")
        private val TAX_PATTERN = Pattern.compile("[0-9]{2}$")

    }


    fun validate(
        email: String,
        password: String,
        phoneNumberSaudi: String,
    ): InvalidInput {
        return when {
            email.isEmpty() -> InvalidInput.EMPTY_EMAIL
            phoneNumberSaudi.isEmpty() -> InvalidInput.EMPTY_PHONE
            password.isEmpty() -> InvalidInput.EMPTY_PASSWORD
            !validateEmail(email) -> InvalidInput.EMAIL
            !validatePhoneSaudi(phoneNumberSaudi)                             -> InvalidInput.PHONE_SAUDI
            !validatePassword(password)                                       -> InvalidInput.PASSWORD
            else                                                              -> InvalidInput.NONE
        }
    }

    fun checkPhone(
        phoneNumberSaudi: String,
    ): InvalidInput {
        return when {
            phoneNumberSaudi.isEmpty() -> InvalidInput.EMPTY_PHONE
            !validatePhoneSaudi(phoneNumberSaudi)                               -> InvalidInput.PHONE_SAUDI
            else                                                              -> InvalidInput.NONE
        }
    }


    fun checkEmail(
        email: String,
    ): InvalidInput {
        return when {
            email.isEmpty()                 -> InvalidInput.EMPTY_EMAIL
            !validateEmail(email)           -> InvalidInput.EMAIL
            else                            -> InvalidInput.NONE
        }
    }

    fun validateEmail(email: String): Boolean = EMAIL_PATTERN.matcher(email).matches()

    private fun validatePassword(password: String): Boolean =
        PASSWORD_PATTERN.matcher(password).matches()

    private fun validatePhoneEgypt(phoneNumber: String): Boolean =
        PHONE_EGYPT_PATTERN.matcher(phoneNumber).matches()

     fun validatePhoneSaudi(phoneNumber: String): Boolean =
        PHONE_SAUDI_PATTERN.matcher(phoneNumber).matches()

    private fun validateText(text: String): Boolean = text.isNotBlank()

    private fun validateCommercialRecord(commercialRecord: String): Boolean =
        COMMERCIAL_RECORD_PATTERN.matcher(commercialRecord).matches()

    private fun validateTaxRecord(taxRecord: String): Boolean =
        TAX_RECORD_PATTERN.matcher(taxRecord).matches()

    private fun validateTax(tax: String): Boolean =
        TAX_PATTERN.matcher(tax).matches()

    fun validateUserData(
        commercialRecord: String? = null,
        commercialRecordName: String? = null,
        tax: String? = null,
        taxRecord: String? = null,
        branchName: String? = null,
        branchAddress: String? = null
    ): InvalidInput {
        return when {
            commercialRecordName.isNullOrEmpty() && commercialRecordName.isNullOrEmpty() && tax.isNullOrEmpty() && taxRecord.isNullOrEmpty() && branchName.isNullOrEmpty() && branchAddress.isNullOrEmpty() -> InvalidInput.EMPTY
            commercialRecord != null && !validateCommercialRecord(commercialRecord) -> InvalidInput.COMMERCIAL_RECORD
            taxRecord != null && ! validateTaxRecord(taxRecord)                     -> InvalidInput.TAX_RECORD
            tax != null && !validateTax(tax)                                        -> InvalidInput.TAX
            branchName.isNullOrEmpty()                                              -> InvalidInput.BRANCH_NAME
            branchAddress.isNullOrEmpty()                                           -> InvalidInput.BRANCH_ADDRESS
            commercialRecordName.isNullOrEmpty()                                    -> InvalidInput.COMMERCIAL_NAME
            else                                                                    -> InvalidInput.NONE
        }
    }

}