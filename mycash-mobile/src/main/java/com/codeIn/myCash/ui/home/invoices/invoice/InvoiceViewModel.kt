package com.codeIn.myCash.ui.home.invoices.invoice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.usecases.GetSingleInvoiceUseCase
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.features.user.domain.accountSettings.usecases.GetInvoiceSettingsUseCase
import com.codeIn.myCash.utilities.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInvoiceSettingsUseCase: GetInvoiceSettingsUseCase,
    private val singleInvoiceUseCase: GetSingleInvoiceUseCase,
    prefs: SharedPrefsModule,
) : ViewModel() {

    private val _invoiceSettingsDataResult =
        MutableStateFlow<InvoiceSettingsState>(InvoiceSettingsState.Idle)
    val invoiceSettingsDataResult = _invoiceSettingsDataResult.asStateFlow()

    private val _invoiceDataResult = MutableStateFlow<InvoiceState>(InvoiceState.Idle)
    val invoiceDataResult = _invoiceDataResult.asStateFlow()

    private val _invoiceModel = MutableLiveData<InvoiceModel?>()
    val invoiceModel: LiveData<InvoiceModel?> = _invoiceModel

    var currency: String? = prefs.getValue(Constants.getCurrency())

    private val _invoiceId = MutableLiveData<String?>()
    val invoiceId: LiveData<String?> = _invoiceId

    private val _newInvoiceId = MutableLiveData<String?>()
    val newInvoiceId: LiveData<String?> = _newInvoiceId

    init {
        launchIO {
            if (savedStateHandle.contains("invoiceId"))
                _invoiceId.postValue(savedStateHandle["invoiceId"])

            invoiceId.asFlow().collect {
                getSingleInvoice()
            }
        }
        getInvoiceSettings()
    }

    private fun getInvoiceSettings() {
        launchIO {
            getInvoiceSettingsUseCase.invoke().let {
                _invoiceSettingsDataResult.emit(it)
            }
        }
    }

    fun getSingleInvoice() {
        _invoiceDataResult.value = InvoiceState.Loading
        launchIO {
            singleInvoiceUseCase.invoke(invoiceId.value).let {
                _invoiceDataResult.emit(it)
            }
        }
    }

    fun updateInvoiceModel(invoiceModel: InvoiceModel?) {
        _invoiceModel.value = invoiceModel
        clearState()
    }

    fun updateNewInvoiceId(invoiceId: String?) {
        _newInvoiceId.value = invoiceId
    }

    fun clearState() = launchIO {
        if(_invoiceDataResult.value != InvoiceState.Idle)
            _invoiceDataResult.emit(InvoiceState.Idle)
    }
}