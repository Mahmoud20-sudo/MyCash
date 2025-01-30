package com.plcoding.reports.domain.usecases

import com.plcoding.reports.domain.postPaidReport.useCases.GetPostpaidReportUseCase
import com.plcoding.reports.domain.productsQuantitiesReport.useCases.GetProductQuantitiesUseCase
import com.plcoding.reports.domain.returnReport.useCases.GetReturnInvoicesReportUseCase
import com.plcoding.reports.domain.salesReport.useCases.GetSalesReportUseCase
import com.plcoding.reports.domain.salesorbuy.usecases.GetSaleInvoicesUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
data class ReportsUseCases @Inject constructor(
    val getSaleInvoices: GetSaleInvoicesUseCase,
    val getSalesReportUseCase: GetSalesReportUseCase,
    val getReturnInvoicesReportUseCase: GetReturnInvoicesReportUseCase,
    val getProductQuantitiesUseCase: GetProductQuantitiesUseCase,
    val getPostpaidReportUseCase: GetPostpaidReportUseCase
)
