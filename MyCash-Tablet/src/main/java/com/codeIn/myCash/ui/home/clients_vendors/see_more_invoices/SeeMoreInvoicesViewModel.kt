package com.codeIn.myCash.ui.home.clients_vendors.see_more_invoices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.common.data.InvoiceFilter.*
import com.codeIn.common.data.Limit
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.usecases.GetInvoicesUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.GetSingleInvoiceByInvoiceNumberUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.GetSingleInvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeeMoreInvoicesViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    prefs : SharedPrefsModule,
    private val getSingleInvoiceByInvoiceNumberUseCase: GetSingleInvoiceByInvoiceNumberUseCase,
    private val getSingleInvoiceUseCase: GetSingleInvoiceUseCase,
    state: SavedStateHandle,
    ) : ViewModel() {

    private val _filter = MutableLiveData<InvoiceFilter>()
    val filter: LiveData<InvoiceFilter> = _filter

    private val _invoiceDataResult = MutableLiveData<InvoiceState>(InvoiceState.Idle)
    val invoiceDataResult : LiveData<InvoiceState> = _invoiceDataResult

    private val _clientId = MutableLiveData<String?>("")
    val clientId : LiveData<String?> = _clientId

    var currency : String? = prefs.getValue(Constants.getCurrency())


    init {
        _filter.postValue(ALL)
        _clientId.value = state["clientId"]
    }

    private fun getInvoices(type : String? = null,
                            isReturn : String? = null,
                            invoiceType : String? = null,
                            saleOrBuy : String? = null,
                            paymentStatus : String? = null,
                            date : String?= null ){
        launchIO {
            getInvoicesUseCase.invoke(
                type = type,
                isReturn = isReturn,
                clientId =  clientId.value,
                invoiceType = invoiceType,
                saleOrBuy = saleOrBuy,
                paymentStatus = paymentStatus ,
                limit = Limit.HUNDRED.value.toString() ,
                date = date ).let {
                _invoiceDataResult.postValue(it)
            }
        }
    }

    fun updateFilter(invoiceFilter: InvoiceFilter , date : String? = null ) {
        if (_filter.value == invoiceFilter) return
        _filter.postValue(invoiceFilter)
        getInvoicesWithFilter(invoiceFilter , date )
    }

    fun clearState(){
        _invoiceDataResult.value = InvoiceState.Idle
    }

    fun getInvoicesWithFilter(invoiceFilter: InvoiceFilter , date : String?){
        when(invoiceFilter){
            ALL -> getInvoices(date= date )
            TAX_INVOICE -> getInvoices(invoiceType = TAX_INVOICE.value.toString() , date = date )
            SIMPLE_INVOICE -> getInvoices(invoiceType = SIMPLE_INVOICE.value.toString() , date = date)
            INSTANT_INVOICE -> getInvoices(type  = INSTANT_INVOICE.value.toString(), date = date)
            PURCHASE_INVOICE -> getInvoices(saleOrBuy  = PURCHASE_INVOICE.value.toString(), date = date)
            SALE_INVOICE -> getInvoices(saleOrBuy = SALE_INVOICE.value.toString(), date = date)
            PURCHASE_RETURNED -> getInvoices(invoiceType  = PURCHASE_INVOICE.value.toString() , isReturn = "1", date = date)
            SALES_RETURNED -> getInvoices(isReturn  = SALES_RETURNED.value.toString(), date = date)
            PAYMENT_COMPLETED -> getInvoices(paymentStatus  = PAYMENT_COMPLETED.value.toString(), date = date)
            PAYMENT_NOT_COMPLETED -> getInvoices(paymentStatus  = PAYMENT_NOT_COMPLETED.value.toString(), date = date)
            else -> getInvoices(date= date )
        }
    }

    fun getInvoiceByInvoiceNumber(invoiceNumber : String?){
        launchIO {
            getSingleInvoiceByInvoiceNumberUseCase.invoke(invoiceNumber).let {
                _invoiceDataResult.postValue(it)
            }
        }
    }

    fun getSingleInvoice(id : String?){
       launchIO {
           getSingleInvoiceUseCase.invoke(id).let {
               _invoiceDataResult.postValue(it)
           }
       }
    }
}