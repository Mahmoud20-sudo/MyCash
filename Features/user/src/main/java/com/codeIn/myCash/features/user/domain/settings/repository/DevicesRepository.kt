package com.codeIn.myCash.features.user.domain.settings.repository

import com.codeIn.myCash.features.user.data.settings.remote.response.device.DevicesState

interface DevicesRepository {

    suspend fun getDevices( countryId : String) : DevicesState
}