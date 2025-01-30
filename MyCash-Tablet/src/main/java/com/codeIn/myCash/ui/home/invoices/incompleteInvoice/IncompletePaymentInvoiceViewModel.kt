package com.codeIn.myCash.ui.home.invoices.incompleteInvoice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.Limit
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.Receipt
import com.codeIn.myCash.ui.home.invoices.Invoice
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.model.CurrentReceiptModel
import com.codeIn.myCash.features.stock.domain.receipt.usecases.GetReceiptsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class IncompletePaymentInvoiceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    prefs : SharedPrefsModule,
    private val getReceiptsUseCase: GetReceiptsUseCase
    ) : ViewModel() {

    private val _invoiceModel = MutableLiveData<InvoiceModel>()
    val invoiceModel: LiveData<InvoiceModel> = _invoiceModel

    var currency : String? = prefs.getValue(Constants.getCurrency())

    private val _receiptDataResult = MutableStateFlow<ReceiptState>(ReceiptState.Idle)
    val receiptDataResult = _receiptDataResult.asStateFlow()

    init {
        if (savedStateHandle.contains("invoice"))
            _invoiceModel.postValue(savedStateHandle["invoice"])
    }

    fun getReceipts(){
        launchIO {
            getReceiptsUseCase.invoke(invoiceId = _invoiceModel.value?.id.toString() , limit = Limit.TWO.value.toString()).let {
                _receiptDataResult.emit(it)
            }
        }
    }
}