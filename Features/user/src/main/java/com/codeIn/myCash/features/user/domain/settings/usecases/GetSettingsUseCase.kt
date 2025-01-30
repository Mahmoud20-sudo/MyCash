package com.codeIn.myCash.features.user.domain.settings.usecases

import com.codeIn.myCash.features.user.data.settings.remote.response.settings.SettingsState
import com.codeIn.myCash.features.user.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke( ): SettingsState {
        return settingsRepository.getSettings()
    }
}