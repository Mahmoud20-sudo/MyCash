package com.codeIn.myCash.integration.model.dataClasses.authentication

import com.codeIn.myCash.integration.model.dataClasses.authentication.devices.Devices
import com.codeIn.myCash.integration.model.dataClasses.authentication.packages.Packages
import com.codeIn.myCash.integration.model.dataClasses.authentication.user.User
import com.codeIn.myCash.integration.utilities.InvalidInput

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class PackagesState {
    data object Idle : PackagesState()
    data object Loading : PackagesState()
    data class Success(val packages: Packages) : PackagesState()
    data class Error(val message: String) : PackagesState()
}

sealed class DevicesState {
    data object Idle : DevicesState()
    data object Loading : DevicesState()
    data class Success(val devices: Devices) : DevicesState()
    data class Error(val message: String) : DevicesState()
}

sealed class UserState {
    data object Idle : UserState()
    data object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
    data class InputError(val invalidInput: InvalidInput) : UserState()
}

sealed class OtpState {
    data object Idle : OtpState()
    data object Loading : OtpState()
    data class CheckCodeSuccess(val message: String) : OtpState()
    data class ResendCodeSuccess(val message: String) : OtpState()
    data class Error(val message: String) : OtpState()
}

sealed class ForgotPasswordState {
    data object Idle : ForgotPasswordState()
    data object Loading : ForgotPasswordState()
    data class Success(val message: String) : ForgotPasswordState()
    data class Error(val message: String) : ForgotPasswordState()
    data class InputError(val invalidInput: InvalidInput) : ForgotPasswordState()
}

sealed class ResetPasswordState {
    data object Idle : ResetPasswordState()
    data object Loading : ResetPasswordState()
    data class Success(val message: String) : ResetPasswordState()
    data class Error(val message: String) : ResetPasswordState()
    data class InputError(val invalidInput: InvalidInput) : ResetPasswordState()
}