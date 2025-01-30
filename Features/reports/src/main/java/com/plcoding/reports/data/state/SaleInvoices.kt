package com.plcoding.reports.data.state

import com.codeIn.common.domain.ErrorEntity
import com.plcoding.reports.data.productsQuantitiesReport.model.response.ProductsQuantityResponse
import com.plcoding.reports.data.returnReport.model.response.ReturnInvoicesReportResponse
import com.plcoding.reports.data.salesReport.model.beans.SalesReport
import com.plcoding.reports.data.salesorbuy.model.SaleOrBuyData

sealed class SaleInvoices {
    data class SuccessSalesReport(val data: List<SalesReport>?) : SaleInvoices()
    data class SuccessSalesInvoices(val data: SaleOrBuyData?) : SaleInvoices()
    data class SuccessReturnInvoice(val data: ReturnInvoicesReportResponse.ReturnInvoicesReportBody?) : SaleInvoices()
    data class SuccessProductsQuantity(val data: ProductsQuantityResponse?) : SaleInvoices()
    data class ServerError(val error: ErrorEntity) : SaleInvoices()
    data class StateError(val message: String?) : SaleInvoices()
    data object UnAuthorized : SaleInvoices()
}