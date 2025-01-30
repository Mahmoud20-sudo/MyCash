package com.codeIn.myCash.features.user.domain.settings.repository

import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
import com.codeIn.myCash.features.user.domain.settings.model.PaymentMethodState

interface SetSubscriptionInfoRepository {

    suspend fun setSubscriptionInfo( selectedPackage : Package? , selectedDevice: DeviceModel? , tax : String?) : PaymentMethodState

}