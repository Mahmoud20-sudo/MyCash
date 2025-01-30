package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState

interface ProfileRepository {
    suspend fun getInfo() : AuthenticationState
    suspend fun enableFeaturesOfSettings(
        drafts : String? ,
        notifications: String? ,
        quickInvoiceMode : String?,
    ) : AuthenticationState
}