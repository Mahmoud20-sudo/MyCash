package com.codeIn.myCash.features.user.domain.settings.usecases

import com.codeIn.myCash.features.user.domain.settings.model.PaymentMethodState
import com.codeIn.myCash.features.user.domain.settings.repository.UpdateSubscriptionInfoRepository
import javax.inject.Inject

class UpdateSubscriptionInfoUseCase @Inject constructor(private val repository: UpdateSubscriptionInfoRepository){

    suspend operator fun invoke(discountValue : String? , paymentMethodState: PaymentMethodState , tax : String?): PaymentMethodState {
        return repository.updateSubscriptionInfo(discountValue , paymentMethodState , tax )
    }
}