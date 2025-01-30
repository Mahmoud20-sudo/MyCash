package com.codeIn.myCash.ui.options.settings.invoice_settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.util.asyncIO
import com.codeIn.common.util.launch
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettings
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.features.user.domain.accountSettings.usecases.GetInvoiceSettingsUseCase
import com.codeIn.myCash.features.user.domain.accountSettings.usecases.StartPurchaseInvoiceOrderNoUseCase
import com.codeIn.myCash.features.user.domain.accountSettings.usecases.StartSaleInvoiceOrderNoUseCase
import com.codeIn.myCash.features.user.domain.accountSettings.usecases.UpdateInvoiceSettingsUseCase
import com.codeIn.myCash.features.user.domain.accountSettings.usecases.UpdateMainInvoiceSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class InvoiceSettingsViewModel @Inject constructor(
    private val invoiceSettings: GetInvoiceSettingsUseCase,
    private val updateInvoiceSettingsUseCase: UpdateInvoiceSettingsUseCase,
    private val updateMainInvoiceSettingsUseCase: UpdateMainInvoiceSettingsUseCase,
    private val startPurchaseInvoiceOrderNoUseCase: StartPurchaseInvoiceOrderNoUseCase,
    private val startSaleInvoiceOrderNoUseCase: StartSaleInvoiceOrderNoUseCase
) : ViewModel() {

    private val _dataResult = MutableLiveData<InvoiceSettingsState>(InvoiceSettingsState.Idle)
    val dataResult: LiveData<InvoiceSettingsState> = _dataResult

    private val _activeInvoiceType = MutableLiveData<Int>(0)
    val activeInvoiceType: LiveData<Int> = _activeInvoiceType

    private val _dataSimpleInvoiceSettings = MutableLiveData<InvoiceSettings?>(null)
    val dataSimpleInvoiceSettings: LiveData<InvoiceSettings?> = _dataSimpleInvoiceSettings

    private val _dataTaxInvoiceSettings = MutableLiveData<InvoiceSettings?>(null)
    val dataTaxInvoiceSettings: LiveData<InvoiceSettings?> = _dataTaxInvoiceSettings

    private val _dataActiveInvoiceSettings = MutableLiveData<InvoiceSettings?>(null)
    val dataActiveInvoiceSettings: LiveData<InvoiceSettings?> = _dataActiveInvoiceSettings

    init {
        getInvoiceSettings()
    }

    private fun getInvoiceSettings() = launchIO {
        invoiceSettings.invoke().let {
            _dataResult.postValue(it)
        }
    }

    fun updateInvoiceSettings(
        commercialName: String?,
        commercialNumber: String?,
        taxRegistrationNumber: String?,
        tax: String?,
        footer: String?,
        productDescription: String?,
        clients: String?,
        cashier: String?,
        zatcaQr: String?,
        myCashQr: String?,
        type: String?,
        active: String?
    ) = asyncIO {
        launch {
            updateInvoiceSettingsUseCase.invoke(
                footerText = footer ?: "",
                productDesc = productDescription,
                client = clients,
                cashier = cashier,
                zatcaQr = zatcaQr,
                myCashQr = myCashQr,
                type = type,
                active = active
            )?.let {
                _dataResult.postValue(it)
            }
        }
        launch {
            updateMainInvoiceSettingsUseCase.invoke(
                commercialName = commercialName, commercialNumber = commercialNumber,
                taxRegistrationNumber = taxRegistrationNumber, tax = tax
            ).let {
                _dataResult.postValue(it)
            }
        }
    }

    fun updateInvoiceType(type: Int, simple: InvoiceSettings?, tax: InvoiceSettings?) = launchIO {
        _activeInvoiceType.postValue(type)
        _dataSimpleInvoiceSettings.postValue(simple)
        _dataTaxInvoiceSettings.postValue(tax)
        when (type) {
            InvoiceType.SIMPLE.value -> {
                _dataActiveInvoiceSettings.postValue(simple)
            }

            InvoiceType.TAX.value -> {
                _dataActiveInvoiceSettings.postValue(tax)
            }
        }
    }

    fun clearState() {
        if (_dataResult.value !is InvoiceSettingsState.Idle)
            _dataResult.value = InvoiceSettingsState.Idle
    }

    fun startSaleInvoiceOrderNo(orderNo: String?) = launchIO {
        startSaleInvoiceOrderNoUseCase.invoke(orderNo).let {
            _dataResult.postValue(it)
        }
    }

    fun startPurchaseInvoiceOrderNo(orderNo: String?) = launchIO {
        startPurchaseInvoiceOrderNoUseCase.invoke(orderNo).let {
            _dataResult.postValue(it)
        }
    }
}