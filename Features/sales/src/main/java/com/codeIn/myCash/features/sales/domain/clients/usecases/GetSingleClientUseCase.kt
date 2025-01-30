package com.codeIn.myCash.features.sales.domain.clients.usecases

import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.repository.ClientsRepository
import javax.inject.Inject

class GetSingleClientUseCase @Inject constructor(private val repository: ClientsRepository){
    suspend operator fun invoke(clientId : String? ,  type: String?) : ClientState {
        return repository.getSingleClient(clientId , type)
    }
}