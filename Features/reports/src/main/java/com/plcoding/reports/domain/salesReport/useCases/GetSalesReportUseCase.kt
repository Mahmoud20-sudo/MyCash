package com.plcoding.reports.domain.salesReport.useCases

import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.domain.salesReport.repository.GetSalesReportRepository
import javax.inject.Inject

class GetSalesReportUseCase @Inject constructor(private val getSalesReportRepository: GetSalesReportRepository) {

    suspend operator fun invoke(request: ReportRequest) = getSalesReportRepository.getSalesReport(request)
}