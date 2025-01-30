package com.plcoding.reports.domain.salesReport.repository

import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.SaleInvoices

interface GetSalesReportRepository {

    suspend fun getSalesReport(request: ReportRequest): SaleInvoices

}