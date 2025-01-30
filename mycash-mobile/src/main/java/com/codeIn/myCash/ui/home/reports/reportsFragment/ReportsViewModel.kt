package com.codeIn.myCash.ui.home.reports.reportsFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.pager.PagerHelper
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO.Data.Data
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.domain.branch.usecase.BranchesUseCase
import com.codeIn.myCash.ui.home.reports.customViews.ReportsFilterTypes.OnSelectFilterTypes
import com.plcoding.reports.data.enums.ReportsFilterTypes
import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.ReportsResponseResult
import com.plcoding.reports.data.state.SaleInvoices
import com.plcoding.reports.data.state.SavableUiState
import com.plcoding.reports.domain.usecases.ReportsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val reportsUseCases: ReportsUseCases,
    private val branchesUseCase: BranchesUseCase,
    val prefs: SharedPrefsModule
) : ViewModel(), OnSelectFilterTypes {

    private var _branches: MutableStateFlow<List<Data>> = MutableStateFlow(emptyList())
    val branches = _branches.asStateFlow()

    private var _reports: MutableStateFlow<ReportsResponseResult> =
        MutableStateFlow(ReportsResponseResult.Loading)
    val reports = _reports.asStateFlow()

    private var _summeryUIControl = MutableStateFlow(ReportsFilterTypes.SALES_REPORTS)
    val summeryUIControl = _summeryUIControl.asStateFlow()

    val currency = prefs.getValue(Constants.getCurrency())

    private var typeOfReport = ReportsFilterTypes.SALES_REPORTS

    private lateinit var request: ReportRequest

    private var _savableState: MutableStateFlow<SavableUiState> = MutableStateFlow(SavableUiState())
    val savableState = _savableState.asStateFlow()

    private val pager = PagerHelper()
    val isLoading = pager.isLoading
    val total get() = pager.total

    val page get() = pager.getNextPage()
    val itemCount get() = pager.total ?: 0
    val isLastPage get() = pager.shouldFetchNextPage()


    init {
        pager.updatePagination(pagination = null, nextPage = 1)
        requestBranches()
    }

    fun getNext() = pager.apply {
        getNext { nextPage, _ ->

            when (typeOfReport) {
                ReportsFilterTypes.SALES_REPORTS -> {
                    // there's no paging
                }

                ReportsFilterTypes.SALES_INVOICES -> {
                    requestSalesInvoice(request.copy(page = nextPage))
                }

                ReportsFilterTypes.RETURN_INVOICES -> {
                    requestReturnInvoice(request.copy(page = nextPage))
                }

                ReportsFilterTypes.PRODUCTS_QUANTITY -> {
                    requestProductQuantities(request.copy(page = nextPage))
                }

                ReportsFilterTypes.POSTPAID -> {
                    requestPostPaid(request.copy(page = nextPage))
                }
            }
        }
    }

    private fun requestPostPaid(request: ReportRequest) = launchIO {
        _reports.emit(ReportsResponseResult.Loading)
        _reports.emit(reportsUseCases.getPostpaidReportUseCase(request))
        _summeryUIControl.emit(ReportsFilterTypes.POSTPAID)
    }

    fun requestBranches() {
        launchIO { checkBranchesState(branchesUseCase()) }
    }

    private fun requestSalesReport(reportRequest: ReportRequest) = launchIO {
        _reports.emit(ReportsResponseResult.Loading)
        val salesInvoices = reportsUseCases.getSalesReportUseCase(reportRequest)

        checkSalesReportState(salesInvoices) { invoices ->

            _reports.emit(invoices)

            _summeryUIControl.emit(ReportsFilterTypes.SALES_REPORTS)
        }
    }

    private fun requestSalesInvoice(request: ReportRequest) = launchIO {
        _reports.emit(ReportsResponseResult.Loading)

        val salesInvoices = reportsUseCases.getSaleInvoices(request)

        checkSaleInvoicesSate(salesInvoices) { invoices ->

            _reports.emit(invoices)

            if (invoices is ReportsResponseResult.SuccessSalesInvoices) {
                invoices.data?.invoices?.data?.let {
                    if (it.isEmpty())
                        _summeryUIControl.emit(ReportsFilterTypes.SALES_REPORTS)
                    else _summeryUIControl.emit(ReportsFilterTypes.SALES_INVOICES)
                }
            }
        }
    }

    private fun requestReturnInvoice(request: ReportRequest) = launchIO {
        _reports.emit(ReportsResponseResult.Loading)

        val salesInvoices = reportsUseCases.getReturnInvoicesReportUseCase(request)

        checkReturnReportState(salesInvoices) { invoices: ReportsResponseResult ->

            _reports.emit(invoices)

            if (invoices is ReportsResponseResult.SuccessReturnReport) {
                invoices.data?.invoices?.data?.let {
                    if (it.isEmpty())
                        _summeryUIControl.emit(ReportsFilterTypes.SALES_REPORTS)
                    else _summeryUIControl.emit(ReportsFilterTypes.RETURN_INVOICES)
                }
            }
        }
    }

    private fun requestProductQuantities(request: ReportRequest) = launchIO {
        _reports.emit(ReportsResponseResult.Loading)

        val response = reportsUseCases.getProductQuantitiesUseCase(request)
        checkProductsQuantitiesState(response) {
            _reports.emit(it)
            _summeryUIControl.emit(ReportsFilterTypes.PRODUCTS_QUANTITY)
        }
    }


    private fun checkBranchesState(result: BranchesState) = launchIO {
        when (result) {
            is BranchesState.SuccessRetrieveBranches -> {
                result.data?.data?.data?.let { _branches.emit(it) }
            }

            is BranchesState.ServerError -> {
                val error: ErrorEntity = result.error
                _reports.emit(ReportsResponseResult.ServerError(error))
            }

            is BranchesState.UnAuthorized -> {
                _reports.emit(ReportsResponseResult.UnAuthorized)
            }

            else -> {
                _reports.emit(ReportsResponseResult.UnknownError)
            }
        }
    }


    private fun checkSaleInvoicesSate(
        result: SaleInvoices,
        salesResult: suspend (ReportsResponseResult) -> Unit
    ) = launchIO {
        when (result) {
            is SaleInvoices.SuccessSalesInvoices -> salesResult(
                ReportsResponseResult.SuccessSalesInvoices(
                    result.data
                )
            )

            is SaleInvoices.ServerError -> salesResult(ReportsResponseResult.ServerError(result.error))
            is SaleInvoices.UnAuthorized -> salesResult(ReportsResponseResult.UnAuthorized)
            else -> salesResult(ReportsResponseResult.UnknownError)
        }
    }


    private fun checkSalesReportState(
        result: SaleInvoices,
        salesResult: suspend (ReportsResponseResult) -> Unit
    ) = launchIO {
        when (result) {
            is SaleInvoices.SuccessSalesReport -> salesResult(
                ReportsResponseResult.SuccessSalesReport(
                    result.data
                )
            )

            is SaleInvoices.ServerError -> salesResult(ReportsResponseResult.ServerError(result.error))
            is SaleInvoices.UnAuthorized -> salesResult(ReportsResponseResult.UnAuthorized)
            else -> salesResult(ReportsResponseResult.UnknownError)
        }
    }


    private fun checkReturnReportState(
        result: SaleInvoices,
        salesResult: suspend (ReportsResponseResult) -> Unit
    ) = launchIO {
        when (result) {
            is SaleInvoices.SuccessReturnInvoice -> salesResult(
                ReportsResponseResult.SuccessReturnReport(result.data)
            )

            is SaleInvoices.ServerError -> salesResult(ReportsResponseResult.ServerError(result.error))
            is SaleInvoices.UnAuthorized -> salesResult(ReportsResponseResult.UnAuthorized)
            else -> salesResult(ReportsResponseResult.UnknownError)
        }
    }

    private fun checkProductsQuantitiesState(
        result: SaleInvoices,
        productQuantities: suspend (ReportsResponseResult) -> Unit
    ) = launchIO {
        when (result) {
            is SaleInvoices.SuccessProductsQuantity -> productQuantities(
                ReportsResponseResult.SuccessProductsQuantity(result.data)
            )

            is SaleInvoices.StateError -> {
                Log.d("Tests", " StateError")
            }

            is SaleInvoices.ServerError -> productQuantities(
                ReportsResponseResult.ServerError(
                    result.error
                )
            )

            is SaleInvoices.UnAuthorized -> productQuantities(ReportsResponseResult.UnAuthorized)
            else -> productQuantities(ReportsResponseResult.UnknownError)
        }
    }


    override fun onSelectReportType(request: ReportRequest) {
        launchIO {
            pager.resetWithPageOne()
            this.request = request

            when (request.typeId) {
                ReportsFilterTypes.SALES_REPORTS.id -> {
                    saveTopReportFilterPosition(
                        ReportsFilterTypes.SALES_REPORTS,
                        request.branchPosition
                    )
                    requestSalesReport(request)
                }

                ReportsFilterTypes.SALES_INVOICES.id -> {
                    saveTopReportFilterPosition(
                        ReportsFilterTypes.SALES_INVOICES,
                        request.branchPosition
                    )
                    requestSalesInvoice(request.copy(page = page))
                }

                ReportsFilterTypes.RETURN_INVOICES.id -> {
                    saveTopReportFilterPosition(
                        ReportsFilterTypes.RETURN_INVOICES,
                        request.branchPosition
                    )
                    requestReturnInvoice(request.copy(page = page))
                }

                ReportsFilterTypes.PRODUCTS_QUANTITY.id -> {
                    saveTopReportFilterPosition(
                        ReportsFilterTypes.PRODUCTS_QUANTITY,
                        request.branchPosition
                    )
                    requestProductQuantities(request.copy(page = page))
                }

                ReportsFilterTypes.POSTPAID.id -> {
                    saveTopReportFilterPosition(ReportsFilterTypes.POSTPAID, request.branchPosition)
                    requestPostPaid(request.copy(page = page))
                }
            }
        }
    }

    fun saveVerticalPosition(position: Int) = runBlocking {
        _savableState.emit(
            _savableState.value.copy(
                selectedVerticalPosition = position
            )
        )
    }

    private suspend fun saveTopReportFilterPosition(type: ReportsFilterTypes, branchPosition: Int) {
        typeOfReport = type

        _savableState.emit(
            _savableState.value.copy(
                selectedReportPosition = type.position,
                selectedBranchPosition = branchPosition
            )
        )
    }

    fun printReport() {

    }

}