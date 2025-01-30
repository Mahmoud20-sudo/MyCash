package com.plcoding.reports.data.returnReport.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.plcoding.reports.data.returnReport.remote.ReturnInvoicesApiService
import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.SaleInvoices
import com.plcoding.reports.domain.returnReport.repository.GetReturnInvoicesRepository
import javax.inject.Inject

class GetReturnInvoicesReportRepositoryImp @Inject constructor(
    private val api: ReturnInvoicesApiService,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : GetReturnInvoicesRepository {

    override suspend fun getReturnInvoices(request: ReportRequest): SaleInvoices {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            api.getReturnInvoices(
                lang = lang,
                token = token,
                page = request.page,
                startDate = request.startDate,
                endDate = request.endDate,
                branchId = request.branchId
            ).let { response ->

                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        SaleInvoices.SuccessReturnInvoice(response.body()?.data)
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