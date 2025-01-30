package com.codeIn.myCash.features.user.data.settings.remote.response.settings

import com.codeIn.common.domain.ErrorEntity

sealed class SettingsState{
    data object Idle : SettingsState()

    data object Loading : SettingsState()

    data class Success(val data : SettingsData?) : SettingsState()

    data class ServerError(val error : ErrorEntity) : SettingsState()

    data class StateError(val message : String?) : SettingsState()
}