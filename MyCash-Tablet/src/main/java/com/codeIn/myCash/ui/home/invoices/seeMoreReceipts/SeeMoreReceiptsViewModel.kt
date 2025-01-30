package com.codeIn.myCash.ui.home.invoices.seeMoreReceipts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.common.data.Limit
import com.codeIn.common.data.ReceiptsFilter
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.usecases.GetReceiptsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SeeMoreReceiptsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    prefs : SharedPrefsModule,
    private val getReceiptsUseCase: GetReceiptsUseCase
    ) : ViewModel() {

    private val _invoiceId = MutableLiveData<String?>()
    val invoiceId: LiveData<String?> = _invoiceId

    private val _filter = MutableLiveData<ReceiptsFilter>()
    val filter: LiveData<ReceiptsFilter> = _filter

    var currency : String? = prefs.getValue(Constants.getCurrency())

    private val _receiptDataResult = MutableLiveData<ReceiptState>(ReceiptState.Idle)
    val receiptDataResult : LiveData<ReceiptState> = _receiptDataResult

    init {
        if (savedStateHandle.contains("invoiceId"))
            _invoiceId.postValue(savedStateHandle["invoiceId"])

        _filter.postValue(ReceiptsFilter.All)
    }

    fun getReceipts(paymentStatus : String? = null , date : String?= null ){
        Log.d("TAG" , "receipts datta for call${_invoiceId.value} , $date")
        launchIO {
            getReceiptsUseCase.invoke(invoiceId = _invoiceId.value , paymentStatus = paymentStatus , date = date).let {
                _receiptDataResult.postValue(it)
                Log.d("TAG" , " receipts $it")
            }
        }
    }

    fun updateFilter(receiptFilter: ReceiptsFilter, date: String?) {
        if (_filter.value == receiptFilter) return
        _filter.postValue(receiptFilter)
        getReceiptsWithFilter(receiptFilter , date )
    }

    fun getReceiptsWithFilter(receiptFilter: ReceiptsFilter , date: String?){
        when(receiptFilter){
            ReceiptsFilter.All -> {
                getReceipts(paymentStatus = null , date=date)
            }
            ReceiptsFilter.COMPLETED -> {
                getReceipts(paymentStatus = ReceiptsFilter.COMPLETED.value.toString() , date = date)
            }
            ReceiptsFilter.UN_COMPLETED -> {
                getReceipts(paymentStatus =  ReceiptsFilter.UN_COMPLETED.value.toString(), date = date)
            }
        }
    }
}