package com.codeIn.myCash.features.user.domain.settings.usecases

import com.codeIn.myCash.features.user.data.settings.remote.response.device.DevicesState
import com.codeIn.myCash.features.user.domain.settings.repository.DevicesRepository
import javax.inject.Inject

class GetDevicesUseCase @Inject constructor(private val devicesRepository: DevicesRepository) {
    suspend operator fun invoke(countryId : String) : DevicesState {
        return devicesRepository.getDevices(countryId)
    }
}