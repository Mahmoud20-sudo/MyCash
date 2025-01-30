package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.domain.authentication.repository.PaymentLinkRepository
import javax.inject.Inject

class PaymentLinkUseCase @Inject constructor(private val repository: PaymentLinkRepository) {

    suspend operator fun invoke(influenceId : String?, packageId : String?, deviceId : String?): GeneralResponse{
        return repository.getPaymentLink(influenceId , packageId, deviceId)
    }
}