package com.codeIn.myCash.features.user.data.settings.repository

import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
import com.codeIn.myCash.features.user.domain.settings.model.PaymentMethodState
import com.codeIn.myCash.features.user.domain.settings.repository.SetSubscriptionInfoRepository
import javax.inject.Inject

class SetSubscriptionInfoRepositoryImpl @Inject constructor(): SetSubscriptionInfoRepository{
    override suspend fun setSubscriptionInfo(selectedPackage : Package?, selectedDevice: DeviceModel? , tax : String?): PaymentMethodState {
        val taxValueText = tax?:"0.0"
        val taxValue = NumberHelper.persianToEnglishText(taxValueText).toDouble()
        val devicePrice =
            NumberHelper.persianToEnglishText(selectedDevice?.price ?: "0.0")
        val packagePrice =
            NumberHelper.persianToEnglishText(selectedPackage?.price ?: "0.0")
        val initialTotal = devicePrice.toDouble() + packagePrice.toDouble()
        var discountTotal = 0.0
        discountTotal = when (selectedPackage?.discountType) {
            "1" -> NumberHelper.persianToEnglishText(selectedDevice?.discount?:"0.0").toDouble() + NumberHelper.persianToEnglishText(selectedPackage?.discount?:"0.0").toDouble()
            "2" -> {
                val discount = NumberHelper.persianToEnglishText(selectedPackage?.discount?:"0.0").toDouble() * packagePrice.toDouble() /100
                NumberHelper.persianToEnglishText(selectedDevice?.discount?:"0.0").toDouble() +discount
            }

            else -> 0.0
        }
        val taxTotal = (initialTotal- discountTotal) * taxValue / 100
        val total = initialTotal - discountTotal
        return PaymentMethodState(
            initialTotal = initialTotal.toString(),
            systemDiscount = NumberHelper.persianToEnglishText(discountTotal.toString()),
            discountValue = "0.0",
            discount = "",
            total = NumberHelper.persianToEnglishText(total.toString()),
            tax = NumberHelper.persianToEnglishText(taxTotal.toString()),
            devicePrice = devicePrice,
            packagePrice = packagePrice
        )
    }
}