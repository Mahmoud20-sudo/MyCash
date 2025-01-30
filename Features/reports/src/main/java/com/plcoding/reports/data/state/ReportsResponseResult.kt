package com.plcoding.reports.data.state

import com.codeIn.common.domain.ErrorEntity
import com.plcoding.reports.data.postPaid.model.response.PostpaidResponse
import com.plcoding.reports.data.productsQuantitiesReport.model.response.ProductsQuantityResponse
import com.plcoding.reports.data.returnReport.model.response.ReturnInvoicesReportResponse
import com.plcoding.reports.data.salesReport.model.beans.SalesReport
import com.plcoding.reports.data.salesorbuy.model.SaleOrBuyData

sealed class ReportsResponseResult {
    data object Loading : ReportsResponseResult()
    data class SuccessSalesInvoices(val data: SaleOrBuyData?) : ReportsResponseResult()
    data class SuccessSalesReport(val data: List<SalesReport>?) : ReportsResponseResult()
    data class SuccessReturnReport(val data: ReturnInvoicesReportResponse.ReturnInvoicesReportBody?) : ReportsResponseResult()
    data class SuccessProductsQuantity(val data: ProductsQuantityResponse?) : ReportsResponseResult()
    data class SuccessPostpaidReport(val result: PostpaidResponse) : ReportsResponseResult()
    data class ServerError(val error: ErrorEntity) : ReportsResponseResult()
    data object UnAuthorized : ReportsResponseResult()
    data object UnknownError : ReportsResponseResult()
}