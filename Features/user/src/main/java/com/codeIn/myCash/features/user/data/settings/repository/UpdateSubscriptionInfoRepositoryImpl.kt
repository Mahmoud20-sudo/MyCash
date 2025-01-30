package com.codeIn.myCash.features.user.data.settings.repository

import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.features.user.domain.settings.model.PaymentMethodState
import com.codeIn.myCash.features.user.domain.settings.repository.UpdateSubscriptionInfoRepository
import javax.inject.Inject

class UpdateSubscriptionInfoRepositoryImpl @Inject constructor(): UpdateSubscriptionInfoRepository {
    override suspend fun updateSubscriptionInfo(
        discountValue: String?,
        paymentMethodState: PaymentMethodState ,
        tax : String?
    ): PaymentMethodState {
        val taxValueText = tax?:"0.0"
        val taxValue = NumberHelper.persianToEnglishText(taxValueText).toDouble()
        val devicePrice =
            NumberHelper.persianToEnglishText(paymentMethodState.devicePrice ?: "0.0")
        val packagePrice =
            NumberHelper.persianToEnglishText(paymentMethodState.packagePrice ?: "0.0")
        val initialTotal = devicePrice.toDouble() + packagePrice.toDouble()
        val discountCoupon = (initialTotal - NumberHelper.persianToEnglishText(paymentMethodState.systemDiscount).toDouble()) *NumberHelper.persianToEnglishText(discountValue?:"0.0").toDouble()/100

        val discountTotal = NumberHelper.persianToEnglishText(paymentMethodState.systemDiscount).toDouble() + NumberHelper.persianToEnglishText(discountCoupon.toString()).toDouble()

        val taxTotal = (initialTotal- discountTotal) * taxValue / 100
        val total = initialTotal - discountTotal
        return PaymentMethodState(
            initialTotal = initialTotal.toString(),
            systemDiscount = paymentMethodState.systemDiscount ,
            discountValue = discountCoupon.toString(),
            discount = paymentMethodState.discount,
            total = NumberHelper.persianToEnglishText(total.toString()),
            tax = NumberHelper.persianToEnglishText(taxTotal.toString()),
            devicePrice = paymentMethodState.devicePrice,
            packagePrice = paymentMethodState.packagePrice
        )
    }
}