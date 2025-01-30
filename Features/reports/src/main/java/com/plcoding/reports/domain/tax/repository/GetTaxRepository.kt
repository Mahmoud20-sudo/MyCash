package com.plcoding.reports.domain.tax.repository

import com.plcoding.reports.data.enums.DateType
import com.plcoding.reports.data.tax.model.TaxState

interface GetTaxRepository {
    suspend fun getReports(
        page: Int?,
        dateFrom: String?,
        dateTo: String?,
        dateType: DateType?
    ): TaxState
}