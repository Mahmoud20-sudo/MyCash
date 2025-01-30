package com.plcoding.reports.domain.expense.usecases

import com.plcoding.reports.data.enums.DateType
import com.plcoding.reports.data.expense.model.ExpenseState
import com.plcoding.reports.domain.expense.repository.GetExpensesRepository
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(private val getExpensesRepository: GetExpensesRepository) {
    suspend operator fun invoke(
        page: Int,
        dateFrom: String?,
        dateTo: String?,
        dateType: DateType?
    ): ExpenseState =
        getExpensesRepository.getReports(
            page = page,
            dateTo = dateTo,
            dataFrom = dateFrom,
            dateType = dateType
        )
}