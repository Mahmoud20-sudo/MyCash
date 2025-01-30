package com.codeIn.myCash.ui.home.invoices.memorandumInvoicePayment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.MemorandumType
import com.codeIn.common.data.PaymentType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.MakeMemorandumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemorandumInvoicePaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    prefs : SharedPrefsModule,
    private val makeMemorandumUseCase: MakeMemorandumUseCase
) :ViewModel() {


    private val _paymentType = MutableLiveData(PaymentType.CASH)
    val paymentType: LiveData<PaymentType> = _paymentType

    var currency : String? = prefs.getValue(Constants.getCurrency())
    var tax : String? = prefs.getValue(AppConstants.TAX)
    private var invoiceId : String? = ""

    private val _memorandumDataResult = MutableLiveData<MemorandumState>(MemorandumState.Idle)
    val memorandumDataResult  : LiveData<MemorandumState> = _memorandumDataResult

    private val _selectedProductsInMemorandum = MutableLiveData<ProductInInvoiceMemorandum?>(null)
    val selectedProductsInMemorandum : LiveData<ProductInInvoiceMemorandum?> = _selectedProductsInMemorandum

    init {
        if (savedStateHandle.contains("products_in_memorandum"))
            _selectedProductsInMemorandum.postValue(savedStateHandle["products_in_memorandum"])

        if (savedStateHandle.contains("invoice_id"))
            invoiceId = savedStateHandle["invoice_id"]
    }

    fun updatePaymentType(type: PaymentType) {
        if (_paymentType.value != type)
            _paymentType.postValue(type)
    }

    fun clearState(){
        _memorandumDataResult.value = MemorandumState.Idle
    }

    fun makeMemorandum(cashValue : String? , visaValue : String? ,  paymentType : String?){
        launchIO {
            _memorandumDataResult.postValue(MemorandumState.Loading)
            makeMemorandumUseCase.invoke(invoiceId.toString() , MemorandumType.DEBTOR.value.toString() ,
                cashValue , visaValue , selectedProductsInMemorandum.value , paymentType).let {
                    _memorandumDataResult.postValue(it)
            }
        }
    }
}