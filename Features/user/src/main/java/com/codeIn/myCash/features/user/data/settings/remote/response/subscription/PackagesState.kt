package com.codeIn.myCash.features.user.data.settings.remote.response.subscription

import com.codeIn.common.domain.ErrorEntity


sealed class PackagesState{
    data object Idle : PackagesState()

    data object Loading : PackagesState()

    data class Success(val data : List<Package>?) : PackagesState()

    data class ServerError(val error : ErrorEntity) : PackagesState()

    data class StateError(val message : String?) : PackagesState()
}