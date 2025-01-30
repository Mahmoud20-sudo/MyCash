package com.codeIn.myCash.ui.home.clients_vendors.update_vendor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.clients.remote.request.AddClientRequest
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.usecases.UpdateClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class UpdateVendorViewModel @Inject constructor(
    state: SavedStateHandle,
    private val updateVendorUseCase: UpdateClientUseCase
) : ViewModel() {

    private val _vendor = MutableStateFlow<ClientModel?>(null)
    val vendor = _vendor.asStateFlow()

    private val _vendorDataResult = MutableStateFlow<ClientState>(ClientState.Idle)
    val vendorDataResult = _vendorDataResult.asStateFlow()


    init {
        _vendor.value = state["vendor"]
    }


    fun updateVendor(request: AddClientRequest) {
        launchIO {
            updateVendorUseCase.invoke(request).let {
                _vendorDataResult.emit(it)
            }
        }
    }

    fun clearState() {
        if (_vendorDataResult.value != ClientState.Idle)
            _vendorDataResult.value = ClientState.Idle
    }

    fun updateCurrentVendor(clientModel: ClientModel?) {
        _vendor.value = clientModel
    }
}
