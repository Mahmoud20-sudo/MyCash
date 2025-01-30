package com.codeIn.myCash.ui.home.invoices.payReceipt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.ui.PaymentType
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptModel
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.model.CurrentReceiptModel
import com.codeIn.myCash.features.stock.domain.receipt.usecases.PayReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PayReceiptViewModel @Inject constructor(
    state: SavedStateHandle,
    prefs : SharedPrefsModule,
    private val payReceiptUseCase: PayReceiptUseCase
    ) : ViewModel() {

    private val _receiptModel = MutableLiveData<ReceiptModel>()
    val receiptModel: LiveData<ReceiptModel> = _receiptModel

    private val _currentReceiptModel = MutableLiveData<CurrentReceiptModel>()
    val currentReceiptModel: LiveData<CurrentReceiptModel> = _currentReceiptModel

    var currency : String? = prefs.getValue(Constants.getCurrency())

    private val _paymentType = MutableLiveData(PaymentType.CASH)
    val paymentType: LiveData<PaymentType> = _paymentType

    private val _receiptDataResult = MutableStateFlow<ReceiptState>(ReceiptState.Idle)
    val receiptDataResult = _receiptDataResult.asStateFlow()
    init {
        if (state.contains("receipt"))
            _receiptModel.postValue(state["receipt"])

        if (state.contains("receiptRequest"))
            _currentReceiptModel.postValue(state["receiptRequest"])
    }
    fun updatePaymentType(type: PaymentType) {
        if (_paymentType.value != type)
            _paymentType.postValue(type)
    }

    fun payReceipt(cashValue : String?  , visaValue : String? , nextDate : String? ,
                   runRefundInvoice : String? = null ,
                   codeRefundInvoice : String? = null ,
                   dateRefundInvoice : String? = null , ){
        launchIO {
            _receiptDataResult.value = ReceiptState.Loading
            payReceiptUseCase.invoke(receiptId = receiptModel.value?.id.toString() ,
                cashPrice = cashValue , visaPrice = visaValue , nextDate= nextDate ,
                runRefundInvoice, codeRefundInvoice, dateRefundInvoice).let {
                _receiptDataResult.emit(it)
            }
        }
    }

    fun clearState(){
        if (receiptDataResult.value != ReceiptState.Idle)
            _receiptDataResult.value = ReceiptState.Idle
    }
}
