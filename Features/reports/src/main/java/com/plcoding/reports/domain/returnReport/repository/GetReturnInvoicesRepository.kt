package com.plcoding.reports.domain.returnReport.repository

import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.SaleInvoices

interface GetReturnInvoicesRepository {

    suspend fun getReturnInvoices(request: ReportRequest): SaleInvoices

}