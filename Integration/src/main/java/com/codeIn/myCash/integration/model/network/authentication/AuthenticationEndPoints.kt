package com.codeIn.myCash.integration.model.network.authentication

object AuthenticationEndPoints {
    const val GET_PACKAGES = "client/general_data/get_packages"
    const val GET_DEVICES = "client/general_data/get_devices"
    const val GET_COUNTRIES = "client/general_data/get_countries"

    const val REGISTER = "client/auth/register"
    const val CHECK_CODE = "client/auth/check_code"
    const val RESEND_CODE = "client/auth/resend_code"
    const val FORGET_PASSWORD = "client/auth/forget_password"
    const val RESET_PASSWORD = "client/auth/reset_password"
    const val LOGIN = "client/auth/login"

}