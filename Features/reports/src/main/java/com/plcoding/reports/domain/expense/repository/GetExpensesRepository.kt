package com.plcoding.reports.domain.expense.repository

import com.plcoding.reports.data.enums.DateType
import com.plcoding.reports.data.expense.model.ExpenseState

interface GetExpensesRepository {
    suspend fun getReports(
        page: Int,
        dataFrom: String?,
        dateTo: String?,
        dateType: DateType?

    ): ExpenseState
}