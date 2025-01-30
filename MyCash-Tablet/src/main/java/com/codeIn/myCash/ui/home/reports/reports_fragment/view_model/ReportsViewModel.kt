package com.codeIn.myCash.ui.home.reports.reports_fragment.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.common.pager.PagerHelper
import com.codeIn.common.util.appendList
import com.plcoding.reports.data.enums.DateType
import com.plcoding.reports.data.enums.InvoiceType
import com.plcoding.reports.data.enums.ReportsTypes
import com.plcoding.reports.data.enums.SaleBuyType
import com.plcoding.reports.data.expense.model.ExpenseModel
import com.plcoding.reports.data.expense.model.ExpenseReport
import com.plcoding.reports.data.expense.model.ExpenseState
import com.plcoding.reports.data.inventory.model.InventoryModel
import com.plcoding.reports.data.inventory.model.InventoryState
import com.plcoding.reports.data.salesorbuy.model.Report
import com.plcoding.reports.data.salesorbuy.model.ReportRequest
import com.plcoding.reports.data.salesorbuy.model.SalesOrBuyModel
import com.plcoding.reports.data.salesorbuy.model.SalesOrBuyState
import com.plcoding.reports.data.tax.model.TaxReportData
import com.plcoding.reports.data.tax.model.TaxState
import com.plcoding.reports.domain.usecases.ReportsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val reportsUseCases: ReportsUseCases
) : ViewModel() {

    private val _getExpenseReportList = MutableStateFlow<ExpenseState>(ExpenseState.Idle)
    val getExpenseReportList = _getExpenseReportList.asStateFlow()

    private val _getTaxReportList = MutableStateFlow<TaxState>(TaxState.Idle)
    val getTaxReportList = _getTaxReportList.asStateFlow()

    private val _getInventoryReportList = MutableStateFlow<InventoryState>(InventoryState.Idle)
    val getInventoryReportList = _getInventoryReportList.asStateFlow()

    private val _getSalesBuyReportList = MutableStateFlow<SalesOrBuyState>(SalesOrBuyState.Idle)
    val getSalesBuyReportList = _getSalesBuyReportList.asStateFlow()

    // Expenses reports
    private val _expenses = MutableLiveData<ArrayList<ExpenseModel>>()
    val expenses: LiveData<ArrayList<ExpenseModel>> = _expenses
    private val _expensesReport = MutableLiveData<ExpenseReport>()
    val expensesReport: LiveData<ExpenseReport> = _expensesReport

    // sales and buy reports
    private val _salesOrBuyList = MutableLiveData<ArrayList<SalesOrBuyModel>>()
    val salesOrBuyList: LiveData<ArrayList<SalesOrBuyModel>> = _salesOrBuyList
    private val _report = MutableLiveData<Report>()
    val report: LiveData<Report> = _report

    // tax reports
    private val _taxReport = MutableLiveData<TaxReportData>()
    val taxReport: LiveData<TaxReportData> = _taxReport

    // inventory reports
    private val _inventoryReports = MutableLiveData<ArrayList<InventoryModel>>()
    val inventoryReports: LiveData<ArrayList<InventoryModel>> = _inventoryReports

    private val _isPaginationLoading = MutableLiveData(false)
    val isPaginationLoading: LiveData<Boolean> = _isPaginationLoading

    private var reportsType = ReportsTypes.MAIN_REPORTS
    private var invoiceType = InvoiceType.NONE
    private var dateType = DateType.NONE
    private var saleOrBuy = SaleBuyType.NONE
    private var dateFrom: String? = null
    private var dateTo: String? = null
    private val pager = PagerHelper()
    val isLoading = pager.isLoading
    val total get() = pager.total
    val page get() = pager.getNextPage()

    val itemCount get() = pager.total ?: 0

    init {
        pager.updatePagination(pagination = null, nextPage = 1)
        callReportApi()
    }

    fun getReportType(): ReportsTypes = reportsType

    fun getInvoiceType(): InvoiceType = invoiceType

    fun getDateType(): DateType = dateType

    fun setDateFrom(value: String) {
        dateFrom = value
        dateType = DateType.NONE
        pager.reset()
        getNext()
    }

    fun setDateTo(value: String) {
        dateTo = value
        dateType = DateType.NONE
        pager.reset()
        getNext()
    }

    private fun getExpenseReport(nextPage: Int) = launchIO {
        _getExpenseReportList.emit(ExpenseState.Loading)
        val result = reportsUseCases.getExpensesUseCase.invoke(
            page = nextPage,
            dateFrom = dateFrom,
            dateTo = dateTo,
            dateType = dateType
        )
        _getExpenseReportList.emit(result)
        if (result is ExpenseState.Success) {
            _expensesReport.postValue(result.data?.expenseReport)
            if (nextPage == 1) {
                _expenses.postValue(result.data?.expenses?.data as ArrayList<ExpenseModel>)
            } else
                _expenses.postValue(_expenses.value.appendList(result.data?.expenses?.data as ArrayList<ExpenseModel>?))

            pager.updatePagination(result.data?.expenses?.pagination)
            _getExpenseReportList.emit(ExpenseState.Idle)
        }
        _isPaginationLoading.postValue(false)
    }


    private fun getTaxReport(nextPage: Int) = launchIO {
        _getTaxReportList.emit(TaxState.Loading)
        val result =
            reportsUseCases.getTaxUseCase.invoke(
                page = nextPage,
                dateFrom = dateFrom,
                dateTo = dateTo,
                dateType = dateType
            )
        _getTaxReportList.emit(result)
        if (result is TaxState.Success) {
            _taxReport.postValue(result.data ?: return@launchIO)
            pager.reset()
            _getTaxReportList.emit(TaxState.Idle)
        }
        _isPaginationLoading.postValue(false)
    }

    private fun getInventoryReport(nextPage: Int) = launchIO {
        _getInventoryReportList.emit(InventoryState.Loading)
        val result =
            reportsUseCases.getInventoryUseCase.invoke(page = nextPage, type = dateType.value)
        _getInventoryReportList.emit(result)
        if (result is InventoryState.SuccessShowReports) {
            if (nextPage == 1) {
                _inventoryReports.postValue(result.data?.data as ArrayList<InventoryModel>)
            } else
                _inventoryReports.postValue(_inventoryReports.value.appendList(result.data?.data as ArrayList<InventoryModel>?))

            pager.updatePagination(result.data?.pagination)
            _getInventoryReportList.emit(InventoryState.Idle)
        }
        _isPaginationLoading.postValue(false)
    }

    private fun getSalesBuyReportReport(nextPage: Int) = launchIO {
        _getSalesBuyReportList.emit(SalesOrBuyState.Loading)
        val result =
            reportsUseCases.getSaleOrUseCases.invoke(
                page = nextPage, reportRequest = ReportRequest(
                    dateFrom = dateFrom,
                    dateTo = dateTo,
                    dateType = dateType.value,
                    saleOrBuy = saleOrBuy.value,
                    invoiceType = invoiceType.value
                )
            )
        _getSalesBuyReportList.emit(result)
        if (result is SalesOrBuyState.SuccessShowReports) {
            _report.postValue(result.data?.report)
            if (nextPage == 1) {
                val items = result.data?.invoices?.data as ArrayList<SalesOrBuyModel>
                _salesOrBuyList.postValue(items)
            } else
                _salesOrBuyList.postValue(_salesOrBuyList.value.appendList(result.data?.invoices?.data as ArrayList<SalesOrBuyModel>?))

            pager.updatePagination(result.data?.invoices?.pagination)
            _getSalesBuyReportList.emit(SalesOrBuyState.Idle)
        }
        _isPaginationLoading.postValue(false)
    }

    private fun callReportApi(nextPage: Int = 1) {
        if (nextPage == 1) {
            _salesOrBuyList.postValue(arrayListOf())
            _inventoryReports.postValue(arrayListOf())
            _expenses.postValue(arrayListOf())
            _isPaginationLoading.postValue(true)
        }

        when (reportsType) {
            ReportsTypes.MAIN_REPORTS -> {
                saleOrBuy = SaleBuyType.NONE
                getSalesBuyReportReport(nextPage)
            }

            ReportsTypes.SALES_REPORTS -> {
                saleOrBuy = SaleBuyType.SALE
                getSalesBuyReportReport(nextPage)
            }

            ReportsTypes.PURCHASING_REPORTS -> {
                saleOrBuy = SaleBuyType.BUY
                getSalesBuyReportReport(nextPage)
            }

            ReportsTypes.EXPENSE_REPORTS -> getExpenseReport(nextPage)
            ReportsTypes.TAX_REPORTS -> getTaxReport(nextPage)
            ReportsTypes.PRODUCT_INVENTORY_REPORTS -> getInventoryReport(nextPage)
        }
    }

    fun onDateChange(type: DateType) {
        dateType = type
        dateFrom = ""
        dateTo = ""
        pager.reset()
        getNext()
    }

    fun onReportChangeType(type: ReportsTypes) {
        reportsType = type
        pager.reset()
        getNext()
    }

    fun onInvoiceTypeChange(type: InvoiceType) {
        invoiceType = type
        pager.reset()
        getNext()
    }

    fun getNext() {
        pager.apply {
            getNext { nextPage, _ ->
                callReportApi(nextPage)
            }
        }
    }
}