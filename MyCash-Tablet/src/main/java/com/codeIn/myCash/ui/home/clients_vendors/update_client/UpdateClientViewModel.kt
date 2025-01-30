package com.codeIn.myCash.ui.home.clients_vendors.update_client

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.usecases.UpdateClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class UpdateClientViewModel @Inject constructor(
    state: SavedStateHandle ,
    private val updateClientUseCase: UpdateClientUseCase
) : ViewModel() {

    private val _client = MutableStateFlow<ClientModel?>(null)
    val client = _client.asStateFlow()

    private val _clientDataResult = MutableStateFlow<ClientState>(ClientState.Idle)
    val clientDataResult = _clientDataResult.asStateFlow()


    init {
        _client.value = state["client"]
    }


    fun updateClient(name : String? , phone : String? ,
                     taxNo: String? , commercialRecordNo : String?,
                     address : String? , extra : String? , email : String?){
        launchIO {
            _clientDataResult.emit(ClientState.Loading)
            updateClientUseCase.invoke(client.value?.id.toString(), name, phone, "1", taxNo, commercialRecordNo, address, extra, email , ClientsSection.CLIENTS.value.toString()).let {
                _clientDataResult.emit(it)
            }
        }
    }
    fun clearState() {
        if (_clientDataResult.value != ClientState.Idle)
            _clientDataResult.value = ClientState.Idle
    }

    fun updateCurrentClient(clientModel: ClientModel?){
        _client.value = clientModel
    }
}
