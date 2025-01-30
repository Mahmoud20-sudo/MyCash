package com.codeIn.myCash.ui.authentication.log_in.otp_verification

data class OtpVerificationState(
    var email : String? = null ,
    var phone : String? = null ,
    var countryId : String? = null ,
    var type : String = ""
)