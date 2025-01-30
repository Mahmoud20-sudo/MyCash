package com.codeIn.myCash.features.user.domain.settings.repository

import com.codeIn.myCash.features.user.data.settings.remote.response.settings.SettingsState

interface SettingsRepository {
    suspend fun getSettings() : SettingsState
}