package com.plcoding.reports.data.salesReport.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.plcoding.reports.data.salesReport.remote.SalesReportApiService
import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.SaleInvoices
import com.plcoding.reports.domain.salesReport.repository.GetSalesReportRepository
import javax.inject.Inject

class GetSalesReportRepositoryImp @Inject constructor(
    private val api: SalesReportApiService,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : GetSalesReportRepository {
    override suspend fun getSalesReport(
        request: ReportRequest
    ): SaleInvoices {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            api.getSalesReport(
                lang = lang,
                token = token,
                startDate = request.startDate,
                endDate = request.endDate,
                interval = request.interval?.name?.lowercase(),
                branchId = request.branchId
            ).let { response ->

                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        SaleInvoices.SuccessSalesReport(response.body()?.data)
                    } else {
                        SaleInvoices.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401) {
                    SaleInvoices.UnAuthorized
                } else {
                    SaleInvoices.ServerError(errorHandler.invoke(response.code()))
                }
            }
        } catch (throwable: Throwable) {
            SaleInvoices.ServerError(errorHandler.getError(throwable))
        }
    }
}