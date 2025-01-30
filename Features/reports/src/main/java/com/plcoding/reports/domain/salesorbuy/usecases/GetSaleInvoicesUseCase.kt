package com.plcoding.reports.domain.salesorbuy.usecases

import com.plcoding.reports.data.reportRequest.ReportRequest

import com.plcoding.reports.data.state.SaleInvoices
import com.plcoding.reports.domain.salesorbuy.repository.GetSaleInvoicesRepository
import javax.inject.Inject

class GetSaleInvoicesUseCase @Inject constructor(
    private val getSaleInvoicesRepository: GetSaleInvoicesRepository
) {
    suspend operator fun invoke(reportRequest: ReportRequest): SaleInvoices =
        getSaleInvoicesRepository.getReports(reportRequest = reportRequest)
}