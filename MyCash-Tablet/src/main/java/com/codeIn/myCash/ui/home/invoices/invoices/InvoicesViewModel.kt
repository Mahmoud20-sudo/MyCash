package com.codeIn.myCash.ui.home.invoices.invoices

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.ViewPager.SavedState
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.common.data.InvoiceFilter.*
import com.codeIn.common.data.Limit
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoicesData
import com.codeIn.myCash.features.stock.domain.invoice.usecases.GetInvoicesUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.GetSingleInvoiceByInvoiceNumberUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.GetSingleInvoiceUseCase
import com.plcoding.reports.data.expense.model.ExpenseReport
import com.plcoding.reports.data.expense.model.ExpenseState
import com.plcoding.reports.data.salesorbuy.model.SalesOrBuyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InvoicesViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getInvoicesUseCase: GetInvoicesUseCase,
    prefs: SharedPrefsModule,
    private val getSingleInvoiceByInvoiceNumberUseCase: GetSingleInvoiceByInvoiceNumberUseCase,
    private val getSingleInvoiceUseCase: GetSingleInvoiceUseCase
) : ViewModel() {

    private val _filter = MutableLiveData<InvoiceFilter>()
    val filter: LiveData<InvoiceFilter> = _filter

    private val _invoiceDataResult = MutableLiveData<InvoiceState>(InvoiceState.Idle)
    val invoiceDataResult: LiveData<InvoiceState> = _invoiceDataResult

    private val _invoices = MutableLiveData<ArrayList<InvoiceModel>>()
    val invoices: LiveData<ArrayList<InvoiceModel>> = _invoices

    var currency: String? = prefs.getValue(Constants.getCurrency())

    var _invoiceDate : String? = null

    init {
        _filter.postValue(ALL)
        getInvoicesWithFilter(ALL, _invoiceDate)
    }

    fun saveState(positions: IntArray){
        state["ARTICLE_SCROLL_POSITION"]  = positions
    }
    val position: IntArray? get() = state.get<IntArray>("ARTICLE_SCROLL_POSITION")

    private fun getInvoices(
        type: String? = null,
        isReturn: String? = null,
        clientId: String? = null,
        invoiceType: String? = null,
        saleOrBuy : String? = null,
        paymentStatus: String? = null,
        date: String? = null
    ) = launchIO {
        getInvoicesUseCase.invoke(
            type,
            isReturn,
            clientId,
            invoiceType,
            saleOrBuy,
            paymentStatus,
            limit = Limit.HUNDRED.value.toString(),
            date = date
        ).let {
            _invoiceDataResult.postValue(it)
            if (it is InvoiceState.SuccessShowInvoices) {
                val items = it.data?.data as ArrayList<InvoiceModel>
                _invoices.postValue(items)
            }
        }
    }

    fun updateFilter(invoiceFilter: InvoiceFilter) {
        if (_filter.value == invoiceFilter) return
        _filter.postValue(invoiceFilter)
        getInvoicesWithFilter(invoiceFilter, _invoiceDate)
    }

    fun clearState() {
        _invoiceDataResult.value = InvoiceState.Idle
    }

    fun getInvoicesWithFilter(invoiceFilter: InvoiceFilter, date: String?) {
        _invoiceDataResult.value = InvoiceState.Loading
        _invoices.postValue(arrayListOf())
        _invoiceDate = date
        when (invoiceFilter) {
            ALL -> getInvoices(date = date)
            TAX_INVOICE -> getInvoices(invoiceType = TAX_INVOICE.value.toString(), date = date)
            SIMPLE_INVOICE -> getInvoices(
                invoiceType = SIMPLE_INVOICE.value.toString(),
                date = date
            )

            INSTANT_INVOICE -> getInvoices(type = INSTANT_INVOICE.value.toString(), date = date)
            PURCHASE_INVOICE -> getInvoices(saleOrBuy = PURCHASE_INVOICE.value.toString(), date = date)

            SALE_INVOICE -> getInvoices(saleOrBuy = SALE_INVOICE.value.toString(), date = date)

            PURCHASE_RETURNED -> getInvoices(
                invoiceType = PURCHASE_INVOICE.value.toString(),
                isReturn = "1",
                date = date
            )

            SALES_RETURNED -> getInvoices(isReturn = SALES_RETURNED.value.toString(), date = date)
            PAYMENT_COMPLETED -> getInvoices(
                paymentStatus = PAYMENT_COMPLETED.value.toString(),
                date = date
            )

            PAYMENT_NOT_COMPLETED -> getInvoices(
                paymentStatus = PAYMENT_NOT_COMPLETED.value.toString(),
                date = date
            )

            else -> getInvoices(date = date)
        }
    }

    fun getInvoiceByInvoiceNumber(invoiceNumber: String?) {
        launchIO {
            getSingleInvoiceByInvoiceNumberUseCase.invoke(invoiceNumber).let {
                _invoiceDataResult.postValue(it)
            }
        }
    }

    fun getSingleInvoice(id: String?) {
        launchIO {
            getSingleInvoiceUseCase.invoke(id).let {
                _invoiceDataResult.postValue(it)
            }
        }
    }
}