package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.repository.ProfileRepository
import javax.inject.Inject

class EnableFeaturesOfSettingsUseCase @Inject constructor(private val repository: ProfileRepository){
    suspend operator fun invoke(drafts : String?, notification : String? , quickInvoiceMode : String?) : AuthenticationState {
        return repository.enableFeaturesOfSettings(drafts = drafts , notifications = notification , quickInvoiceMode= quickInvoiceMode)
    }
}