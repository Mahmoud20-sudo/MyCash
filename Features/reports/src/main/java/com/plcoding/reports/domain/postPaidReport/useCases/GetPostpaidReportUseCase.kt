package com.plcoding.reports.domain.postPaidReport.useCases

import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.domain.postPaidReport.repository.GetPostPaidRepository
import javax.inject.Inject

class GetPostpaidReportUseCase @Inject constructor(private val getPostPaidRepository: GetPostPaidRepository) {
    suspend operator fun invoke(request: ReportRequest) = getPostPaidRepository.getPostPaid(request)

}