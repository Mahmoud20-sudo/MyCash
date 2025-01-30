package com.codeIn.myCash.ui.home.reports.salesDetailFragment

import androidx.lifecycle.ViewModel
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.pager.PagerHelper
import com.codeIn.common.util.launchIO
import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.SaleInvoices
import com.plcoding.reports.data.state.ReportsResponseResult
import com.plcoding.reports.domain.usecases.ReportsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SalesReportDetailVM @Inject constructor(
    private val reportsUseCases: ReportsUseCases,
    val prefs: SharedPrefsModule
) : ViewModel() {

    private var _salesInvoices: MutableStateFlow<ReportsResponseResult> = MutableStateFlow(ReportsResponseResult.Loading)
    val salesInvoices = _salesInvoices.asStateFlow()

    val currency = prefs.getValue(Constants.getCurrency())

    private val pager = PagerHelper()
    val page get() = pager.getNextPage()
    val isLastPage get() = pager.shouldFetchNextPage()
    private lateinit var request: ReportRequest


    fun getSalesReportDetail(request: ReportRequest) {
        this.request = request
        requestSalesInvoice(request)

    }

    fun getNext() = pager.apply {
        getNext { nextPage, _ ->
            requestSalesInvoice(request.copy(page = nextPage))
        }
    }

    private fun requestSalesInvoice(reportRequest: ReportRequest) = launchIO {
        _salesInvoices.emit(ReportsResponseResult.Loading)

        val salesInvoices = reportsUseCases.getSaleInvoices(reportRequest)

        checkSaleInvoicesSate(salesInvoices) { invoices ->
            _salesInvoices.emit(invoices)
        }
    }

    private fun checkSaleInvoicesSate(
        result: SaleInvoices,
        salesResult: suspend (ReportsResponseResult) -> Unit
    ) = launchIO {
        when (result) {
            is SaleInvoices.SuccessSalesInvoices -> salesResult(
                ReportsResponseResult.SuccessSalesInvoices(result.data)
            )
            is SaleInvoices.ServerError -> salesResult(ReportsResponseResult.ServerError(result.error))
            is SaleInvoices.UnAuthorized -> salesResult(ReportsResponseResult.UnAuthorized)
            else -> salesResult(ReportsResponseResult.UnknownError)
        }
    }
}