package com.plcoding.reports.domain.salesorbuy.repository

import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.SaleInvoices

interface GetSaleInvoicesRepository {
    suspend fun getReports(reportRequest: ReportRequest): SaleInvoices
}