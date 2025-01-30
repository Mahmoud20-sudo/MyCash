package com.plcoding.reports.data.tax.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.plcoding.reports.data.enums.DateType
import com.plcoding.reports.data.tax.model.TaxState
import com.plcoding.reports.data.tax.remote.ApiTaxReports
import com.plcoding.reports.domain.tax.repository.GetTaxRepository
import javax.inject.Inject

class GetTaxRepositoryImpl @Inject constructor(
    private val taxApi: ApiTaxReports,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : GetTaxRepository {
    override suspend fun getReports(
        page: Int?,
        dateFrom: String?,
        dateTo: String?,
        dateType: DateType?
    ): TaxState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            taxApi.getTaxReports(
                lang = lang,
                authorization = token,
                page = page,
                dateFrom = dateFrom,
                dateTo = dateTo,
                dateType = dateType?.value
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        TaxState.Success(response.body()?.taxReportData)
                    } else {
                        TaxState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401) {
                    TaxState.UnAuthorized
                } else {
                    TaxState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        } catch (throwable: Throwable) {
            TaxState.ServerError(errorHandler.getError(throwable))
        }
    }
}