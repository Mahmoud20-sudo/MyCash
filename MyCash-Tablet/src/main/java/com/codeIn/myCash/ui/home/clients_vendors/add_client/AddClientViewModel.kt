package com.codeIn.myCash.ui.home.clients_vendors.add_client

import androidx.lifecycle.ViewModel
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class AddClientViewModel @Inject constructor(
    private val createClientUseCase: CreateClientUseCase
) : ViewModel() {

    private val _clientDataResult = MutableStateFlow<ClientState>(ClientState.Idle)
    val clientDataResult = _clientDataResult.asStateFlow()


    fun createClient(name : String? , phone : String? ,
                     taxNo: String? , commercialRecordNo : String?,
                     address : String? , extra : String? , email : String?){
        launchIO {
            createClientUseCase.invoke(name, phone, "1", taxNo, commercialRecordNo, address, extra, email , ClientsSection.CLIENTS.value.toString()).let {
                _clientDataResult.emit(it)
            }
        }
    }
    fun clearState() {
        if (_clientDataResult.value != ClientState.Idle)
            _clientDataResult.value = ClientState.Idle
    }

}
