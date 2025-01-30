package com.codeIn.myCash.ui.home.clients_vendors.vendor_details

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
class VendorDetailsViewModel @Inject constructor(
    state: SavedStateHandle,
    private val deleteVendorUseCase: DeleteClientUseCase,
    private val getInvoicesUseCase: GetInvoicesUseCase,
    prefs : SharedPrefsModule,
) :ViewModel() {

    private val _vendorDataResult = MutableStateFlow<ClientState>(ClientState.Idle)
    val vendorDataResult = _vendorDataResult.asStateFlow()

    private val _invoicesDataResult = MutableStateFlow<InvoiceState>(InvoiceState.Idle)
    val invoicesDataResult = _invoicesDataResult.asStateFlow()

    private val _vendor = MutableStateFlow<ClientModel?>(null)
    val vendor = _vendor.asStateFlow()

    var currency : String? = prefs.getValue(Constants.getCurrency())

    init {
        _vendor.value = state["vendor"]
    }

    fun clearVendorState(){
        if (_vendorDataResult.value != ClientState.Idle)
            _vendorDataResult.value = ClientState.Idle
    }

    fun deleteVendor(){
        launchIO {
            _vendorDataResult.emit(ClientState.Loading)
            deleteVendorUseCase.invoke(vendor.value?.id.toString() , ClientsSection.VENDORS.value.toString()).let { client->
                _vendorDataResult.emit(client)
            }
        }
    }

    fun updateVendor(vendorModel: ClientModel?){
        _vendor.value = vendorModel
    }

    fun getInvoices(){
        launchIO {
            getInvoicesUseCase.invoke(type = null , isReturn = null , clientId = vendor.value?.id.toString() ,
                invoiceType = null , saleOrBuy = null , paymentStatus = null, limit = Limit.TWO.value.toString() , date = null ).let {
                _invoicesDataResult.emit(it)
            }
        }
    }

}
