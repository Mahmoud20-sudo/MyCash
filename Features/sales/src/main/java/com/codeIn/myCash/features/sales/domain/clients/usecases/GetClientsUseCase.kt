package com.codeIn.myCash.features.sales.domain.clients.usecases

import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.repository.ClientsRepository
import javax.inject.Inject

class GetClientsUseCase @Inject constructor(private val repository: ClientsRepository){
    suspend operator fun invoke(phone : String? , searchText : String? , paymentStatus : String? , type : String? , limit : String?) : ClientState {
        return repository.getClients(phone, searchText, paymentStatus, type , limit)
    }
}