package com.codeIn.myCash.integration.domain.authentication.login

import com.codeIn.myCash.integration.model.dataClasses.authentication.ForgotPasswordState
import com.codeIn.myCash.integration.model.dataClasses.authentication.ResetPasswordState
import com.codeIn.myCash.integration.model.dataClasses.authentication.UserState


interface LogInRepository {
    /**
     * suspend function to log in a user.
     * @param phone: [String] user phone number, length should be 9 and starts with 5.
     * @param password: [String] user password, should be at least 6 characters and contains at least one special character.
     * @param countryId: [Int] user country id.
     * @return [UserState] containing the user data if the login was successful, or an error message if not.
     */
    suspend fun logIn(phone: String, password: String, countryId: Int): UserState
}


interface ForgotPasswordRepository {
    /**
     * suspend function to send a forgot password request.
     * @param phone: [String] user phone number, length should be 9 and starts with 5.
     * @param email: [String] user email, should be a valid email format.
     * @param countryId: [Int] user country id.
     * @return [ForgotPasswordState] containing a success message if the request was successful, or an error message if not.
     */
    suspend fun forgotPassword(
        phone: String? = null,
        email: String? = null,
        countryId: Int
    ): ForgotPasswordState
}

interface ResetPasswordRepository {

    /**
     * suspend function to reset user password.
     * @param password: [String] user new password, should be at least 6 characters and contains at least one special character.
     * @param confirmPassword: [String] user new password confirmation, should be the same as the new password.
     * @return [ResetPasswordState] containing a success message if the request was successful, or an error message if not.
     */
    suspend fun resetPassword(password: String, confirmPassword: String): ResetPasswordState
}

