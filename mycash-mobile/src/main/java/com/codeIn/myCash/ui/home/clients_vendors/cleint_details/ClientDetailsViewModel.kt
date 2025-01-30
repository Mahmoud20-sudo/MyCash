package com.codeIn.myCash.ui.home.clients_vendors.cleint_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.data.Limit
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.Payment
import com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.Receipt
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.usecases.DeleteClientUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.GetSingleClientUseCase
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.invoice.usecases.GetInvoicesUseCase
import com.codeIn.myCash.features.stock.domain.receipt.usecases.GetReceiptsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ClientDetailsViewModel @Inject constructor(
    state: SavedStateHandle,
    private val deleteClientUseCase: DeleteClientUseCase,
    private val getInvoicesUseCase: GetInvoicesUseCase,
    prefs : SharedPrefsModule,
    private val getReceiptsUseCase: GetReceiptsUseCase
) :ViewModel() {


    private val _clientDataResult = MutableStateFlow<ClientState>(ClientState.Idle)
    val clientDataResult = _clientDataResult.asStateFlow()

    private val _invoicesDataResult = MutableStateFlow<InvoiceState>(InvoiceState.Idle)
    val invoicesDataResult = _invoicesDataResult.asStateFlow()

    private val _receiptsDataResult = MutableStateFlow<ReceiptState>(ReceiptState.Idle)
    val receiptsDataResult = _receiptsDataResult.asStateFlow()

    private val _client = MutableStateFlow<ClientModel?>(null)
    val client = _client.asStateFlow()

    var currency : String? = prefs.getValue(Constants.getCurrency())
    init {
        _client.value = state["client"]
    }

    fun clearClientState(){
        if (_clientDataResult.value != ClientState.Idle)
            _clientDataResult.value = ClientState.Idle
    }

    fun deleteClient(){
        launchIO {
            _clientDataResult.emit(ClientState.Loading)
            deleteClientUseCase.invoke(client.value?.id.toString() ,ClientsSection.CLIENTS.value.toString()).let {client->
                _clientDataResult.emit(client)
            }
        }
    }

    fun updateClient(clientModel: ClientModel?){
        _client.value = clientModel
    }

    fun getInvoices(){
        launchIO {
            getInvoicesUseCase.invoke(type = null , isReturn = null , clientId = client.value?.id.toString() ,
                invoiceType = null , saleOrBuy = null, paymentStatus = null , limit = Limit.TWO.value.toString() , date = null ).let {
                _invoicesDataResult.emit(it)
            }
        }
    }

    fun getReceipts(){
        launchIO {
            getReceiptsUseCase.invoke(clientId = client.value?.id.toString() ,
                limit = Limit.TWO.value.toString()).let {
                _receiptsDataResult.emit(it)
            }
        }
    }
}
