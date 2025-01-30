package com.plcoding.reports.domain.productsQuantitiesReport.repository

import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.SaleInvoices

interface GetProductsQuantityRepository {
    suspend fun getProductsQuantityReport(request: ReportRequest): SaleInvoices
}