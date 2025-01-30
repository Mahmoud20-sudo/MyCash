package com.codeIn.myCash.features.sales.data.clients.remote.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class ClientState{

    data object Idle : ClientState()

    data object Loading : ClientState()

    data class SuccessShowClients(val data : ClientsData?) : ClientState()

    data class SuccessShowSingleClient(val data : ClientModel?) : ClientState()

    data class SuccessDeleteClient(val message: String?) : ClientState()

    data class ServerError(val error : ErrorEntity) : ClientState()

    data class StateError(val message : String?) : ClientState()

    data class InputError(val inputError : InvalidInput) : ClientState()

    data object UnAuthorized: ClientState()
}