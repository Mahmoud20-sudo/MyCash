package com.codeIn.myCash.features.user.domain.settings.usecases

import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
import com.codeIn.myCash.features.user.domain.settings.model.PaymentMethodState
import com.codeIn.myCash.features.user.domain.settings.repository.SetSubscriptionInfoRepository
import javax.inject.Inject

class SetSubscriptionInfoUseCase @Inject constructor(private val repository: SetSubscriptionInfoRepository){

    suspend operator fun invoke(selectedPackage : Package?, selectedDevice: DeviceModel? , tax : String?): PaymentMethodState {
        return repository.setSubscriptionInfo(selectedPackage, selectedDevice , tax )
    }
}