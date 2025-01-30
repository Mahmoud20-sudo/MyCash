package com.codeIn.myCash.integration.domain.authentication.signUp

import com.codeIn.myCash.integration.model.dataClasses.authentication.DevicesState
import com.codeIn.myCash.integration.model.dataClasses.authentication.OtpState
import com.codeIn.myCash.integration.model.dataClasses.authentication.PackagesState
import com.codeIn.myCash.integration.model.dataClasses.authentication.UserState


interface PackagesRepository {

    /**
     * suspend function to get packages.
     * @return [PackagesState] containing the packages data if the request was successful, or an error message if not.
     */
    suspend fun getPackages(): PackagesState
}

interface DevicesRepository {

    /**
     * suspend function to get devices.
     * @return [DevicesState] containing the devices data if the request was successful, or an error message if not.
     */
    suspend fun getDevices(): DevicesState
}

interface RegisterRepository {

    /**
     * suspend function to register a user.
     * @param packageId: [Int] user package id.
     * @param deviceId: [Int] user device id.
     * @param countryId: [Int] user country id.
     * @param phoneNumber: [String] user phone number, length should be 9 and starts with 5.
     * @param password: [String] user password, should be at least 6 characters and contains at least one special character.
     * @param email: [String] user email, should be a valid email format.
     * @param influencerId: [String] user influencer id.
     * @return [UserState] containing the user data if the registration was successful, or an error message if not.
     */
    suspend fun register(
        packageId: Int,
        deviceId: Int,
        countryId: Int,
        phoneNumber: String,
        password: String,
        email: String,
        influencerId: String
    ): UserState
}

interface OtpRepository {

    /**
     * suspend function to check otp code.
     * @param phoneNumber: [String] user phone number, length should be 9 and starts with 5.
     * @param email: [String] user email, should be a valid email format.
     * @param otpCode: [String] user otp code, should be 6 digits.
     * @param countryId: [Int] user country id.
     * @return [OtpState] containing a success message if the request was successful, or an error message if not.
     */
    suspend fun checkOtpCode(
        phoneNumber: String? = null,
        email: String? = null,
        otpCode: String,
        countryId: Int
    ): OtpState

    /**
     * suspend function to resend otp code.
     * @param phoneNumber: [String] user phone number, length should be 9 and starts with 5.
     * @param email: [String] user email, should be a valid email format.
     * @param countryId: [Int] user country id.
     * @return [OtpState] containing a success message if the request was successful, or an error message if not.
     */
    suspend fun resendOtpCode(
        phoneNumber: String? = null,
        email: String? = null,
        countryId: Int
    ): OtpState
}

