package com.plcoding.reports.data.postPaid.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.plcoding.reports.data.postPaid.remote.PostpaidApiService
import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.ReportsResponseResult
import com.plcoding.reports.domain.postPaidReport.repository.GetPostPaidRepository
import javax.inject.Inject

class GetPostPaidRepositoryImp @Inject constructor(
    private val api: PostpaidApiService,
    private val errorHandler: ErrorHandlerImpl,
    private val prefs: SharedPrefsModule
) : GetPostPaidRepository {
    override suspend fun getPostPaid(request: ReportRequest): ReportsResponseResult {
        return try {
            val response = api.getPostpaidReports(
                lang = prefs.getValue(Constants.getLang()),
                token = prefs.getValue(Constants.getToken()),
                startDate = request.startDate,
                endDate = request.endDate,
                invoiceNumber = request.invoiceNum?.toInt(),
                receiptNumber = request.receiptNum?.toInt(),
                receiptStatusId = request.receiptStatusId ?: 0,
                branchId = request.branchId,
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.status == 1) ReportsResponseResult.SuccessPostpaidReport(it)
                    else ReportsResponseResult.ServerError(errorHandler.invoke(response.code()))
                } ?: run { ReportsResponseResult.UnknownError }
            }
            else if (response.code() == 401) ReportsResponseResult.UnAuthorized
            else ReportsResponseResult.ServerError(errorHandler.invoke(response.code()))

        } catch (ex: Throwable) {
            ex.printStackTrace()
            ReportsResponseResult.ServerError(errorHandler.getError(ex))
        }
    }
}