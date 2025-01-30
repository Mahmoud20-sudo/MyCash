package com.plcoding.reports.domain.postPaidReport.repository

import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.ReportsResponseResult

interface GetPostPaidRepository {
    suspend fun getPostPaid(request: ReportRequest): ReportsResponseResult
}