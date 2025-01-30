package com.codeIn.myCash.features.user.domain.settings.repository

import com.codeIn.myCash.features.user.domain.settings.model.PaymentMethodState

interface UpdateSubscriptionInfoRepository {
    suspend fun updateSubscriptionInfo(discountValue : String? , paymentMethodState: PaymentMethodState, tax : String?) : PaymentMethodState
}