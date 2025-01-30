package com.codeIn.myCash.features.user.domain.settings.repository

import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.PackagesState

interface PackagesRepository {
    suspend fun getPackages( countryId : String?) : PackagesState
}