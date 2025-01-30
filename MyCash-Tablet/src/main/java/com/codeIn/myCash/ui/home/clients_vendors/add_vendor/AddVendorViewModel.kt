package com.codeIn.myCash.ui.home.clients_vendors.add_vendor

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
class AddVendorViewModel @Inject constructor(
    private val createVendorUseCase: CreateClientUseCase
) : ViewModel() {

    private val _vendorDataResult = MutableStateFlow<ClientState>(ClientState.Idle)
    val vendorDataResult = _vendorDataResult.asStateFlow()


    fun createClient(name : String? , phone : String? ,
                     taxNo: String? , commercialRecordNo : String?,
                     address : String? , extra : String? , email : String?){
        launchIO {
            createVendorUseCase.invoke(name, phone, "1", taxNo, commercialRecordNo, address, extra, email , ClientsSection.VENDORS.value.toString()).let {
                _vendorDataResult.emit(it)
            }
        }
    }
    fun clearState() {
        if (_vendorDataResult.value != ClientState.Idle)
            _vendorDataResult.value = ClientState.Idle
    }

}
