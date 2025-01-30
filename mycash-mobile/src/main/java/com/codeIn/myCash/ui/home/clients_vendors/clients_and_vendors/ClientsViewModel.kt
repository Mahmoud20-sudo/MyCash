package com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.ClientsFilter
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.data.Limit
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.usecases.GetClientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * view model for the clients fragment
 * it is used to handle the vendorsData for the clients fragment and the clients list and filter and main section
 * @see ClientsFragment
 */
@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val getClientsUseCase : GetClientsUseCase,
) : ViewModel() {

    private val _dataResult = MutableStateFlow<ClientState>(ClientState.Idle)
    val dataResult = _dataResult.asStateFlow()

    private val _mainSection = MutableStateFlow<ClientsSection>(ClientsSection.CLIENTS)
    val mainSection= _mainSection.asStateFlow()

    private val _filter = MutableLiveData<ClientsFilter>(ClientsFilter.ALL)
    val filter : LiveData<ClientsFilter> = _filter

    private var _searchText = MutableLiveData<String?>(null)
    val searchText : LiveData<String?> = _searchText

    init {
        getClients(null  , null )
    }


    fun getClients(phone : String? = null  , paymentStatus : String? = null){
        launchIO {
            getClientsUseCase.invoke(phone, searchText.value , paymentStatus, mainSection.value.value.toString() , Limit.HUNDRED.value.toString() ).let {response->
                _dataResult.emit(response)
            }
        }
    }


    fun updateMainSection(clientsSection: ClientsSection) {
        if (_mainSection.value != clientsSection) {
            _mainSection.value = clientsSection
            updateFilter(ClientsFilter.ALL)
        }
    }


    fun clearClientState(){
        if (_dataResult.value != ClientState.Idle)
            _dataResult.value = ClientState.Idle
    }

    fun updateSearchText(query : String?){
        _searchText.postValue(query)
    }

    fun updateFilter(clientsFilter: ClientsFilter) {
        if (_filter.value == clientsFilter) return
        _filter.postValue(clientsFilter)
        when(clientsFilter){
            ClientsFilter.ALL -> getClients(paymentStatus = "")
            ClientsFilter.INSTALLMENT_PAYMENT -> getClients(paymentStatus = ClientsFilter.INSTALLMENT_PAYMENT.value.toString())
            ClientsFilter.PAYMENT_COMPLETED -> getClients(paymentStatus = ClientsFilter.PAYMENT_COMPLETED.value.toString())
        }
    }



}
