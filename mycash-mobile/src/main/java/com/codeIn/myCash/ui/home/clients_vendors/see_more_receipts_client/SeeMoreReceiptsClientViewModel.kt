package com.codeIn.myCash.ui.home.clients_vendors.see_more_receipts_client

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
class SeeMoreReceiptsClientViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    prefs : SharedPrefsModule,
    private val getReceiptsUseCase: GetReceiptsUseCase,
    ) : ViewModel() {

    private val _clientId = MutableLiveData<String?>()
    val clientId : LiveData<String?> = _clientId

    private val _filter = MutableLiveData<ReceiptsFilter>()
    val filter: LiveData<ReceiptsFilter> = _filter

    var currency : String? = prefs.getValue(Constants.getCurrency())

    private val _receiptDataResult = MutableLiveData<ReceiptState>(ReceiptState.Idle)
    val receiptDataResult : LiveData<ReceiptState> = _receiptDataResult

    init {
        _filter.postValue(ReceiptsFilter.All)
        _clientId.value = savedStateHandle["clientId"]
    }

    fun getReceipts(paymentStatus : String? = null , date : String?= null ){
        launchIO {
            getReceiptsUseCase.invoke(invoiceId = null , paymentStatus = paymentStatus , date = date , clientId = clientId.value ,
                limit  = Limit.HUNDRED.value.toString()).let {
                _receiptDataResult.postValue(it)
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