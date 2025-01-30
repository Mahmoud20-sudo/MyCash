package com.codeIn.myCash.features.sales.domain.clients.repository

import com.codeIn.myCash.features.sales.data.clients.remote.request.AddClientRequest
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState

interface ClientsRepository {

    suspend fun getClients (phone : String? , searchText : String? , paymentStatus : String? , type : String? , limit : String?) : ClientState

    suspend fun getSingleClient(clientId : String? ,  type: String?): ClientState

    suspend fun deleteClient(clientId : String? ,  type: String?): ClientState

    suspend fun createClient(request: AddClientRequest): ClientState

    suspend fun updateClient( request: AddClientRequest): ClientState
}