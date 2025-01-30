package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.common.data.AuthType
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.ValidateEmailUseCase
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import com.codeIn.common.domain.usecases.ValidatePhoneSaudiUseCase
import javax.inject.Inject

class SendOtpValidationUseCase @Inject constructor(
    private val phoneSaudiUseCase: ValidatePhoneSaudiUseCase,
    private val emailUseCase: ValidateEmailUseCase,
    private val emptyUseCase: ValidateEmptyUseCase
) {
    operator fun invoke( key: String?, type: String ) : InvalidInput{
        when (type) {
            AuthType.PHONE.value.toString() -> {
                if (emptyUseCase.invoke(key))
                    return InvalidInput.EMPTY
                else if(!phoneSaudiUseCase.invoke(key))
                    return InvalidInput.PHONE_SAUDI
            }
            AuthType.EMAIL.value.toString() -> {
                if(emptyUseCase.invoke(key))
                    return InvalidInput.EMPTY
                else if (!emailUseCase.invoke(key))
                    return InvalidInput.EMAIL
                }
            else -> return InvalidInput.NONE
            }
        return InvalidInput.NONE
    }

}
