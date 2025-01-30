package com.plcoding.reports.data.salesorbuy.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.SaleInvoices
import com.plcoding.reports.data.salesorbuy.remote.SaleBuyReportsApiService
import com.plcoding.reports.domain.salesorbuy.repository.GetSaleInvoicesRepository
import javax.inject.Inject

class GetSaleInvoicesRepositoryImpl @Inject constructor(
    private val saleBuyApi: SaleBuyReportsApiService,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : GetSaleInvoicesRepository {

    override suspend fun getReports(reportRequest: ReportRequest): SaleInvoices {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            saleBuyApi.getSaleInvoices(
                lang = lang,
                authorization = token,
                page =  reportRequest.page,
                dateFrom = reportRequest.startDate,
                dateTo = reportRequest.endDate,
                branchId = reportRequest.branchId
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        SaleInvoices.SuccessSalesInvoices(response.body()?.saleOrBuyData)
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