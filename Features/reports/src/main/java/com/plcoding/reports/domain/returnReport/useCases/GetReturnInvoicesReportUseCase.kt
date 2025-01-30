package com.plcoding.reports.domain.returnReport.useCases

import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.domain.returnReport.repository.GetReturnInvoicesRepository
import javax.inject.Inject

class GetReturnInvoicesReportUseCase @Inject constructor(
    private val getReturnInvoicesRepository: GetReturnInvoicesRepository
) {

    suspend operator fun invoke(request: ReportRequest) =
        getReturnInvoicesRepository.getReturnInvoices(request)
}