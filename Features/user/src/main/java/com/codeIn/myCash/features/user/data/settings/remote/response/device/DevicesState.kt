package com.codeIn.myCash.features.user.data.settings.remote.response.device

import com.codeIn.common.domain.ErrorEntity

sealed class DevicesState{

    data object Idle : DevicesState()

    data object Loading : DevicesState()

    data class Success(val data : DevicesData?) : DevicesState()

    data class ServerError(val error : ErrorEntity) : DevicesState()

    data class StateError(val message : String?) : DevicesState()
}