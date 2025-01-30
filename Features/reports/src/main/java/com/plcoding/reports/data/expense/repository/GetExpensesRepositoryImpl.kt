package com.plcoding.reports.data.expense.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.plcoding.reports.data.enums.DateType
import com.plcoding.reports.data.expense.model.ExpenseState
import com.plcoding.reports.data.expense.remote.ApiExpenseReports
import com.plcoding.reports.domain.expense.repository.GetExpensesRepository
import javax.inject.Inject

class GetExpensesRepositoryImpl @Inject constructor(
    private val expenseApi: ApiExpenseReports,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : GetExpensesRepository {
    override suspend fun getReports(
        page: Int,
        dataFrom: String?,
        dateTo: String?,
        dateType: DateType?
    ): ExpenseState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            expenseApi.getExpenses(
                lang = lang,
                authorization = token,
                page = page,
                dateFrom = dataFrom,
                dateTo = dateTo,
                dateType = dateType?.value
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        ExpenseState.Success(response.body()?.expensesData)
                    } else {
                        ExpenseState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401) {
                    ExpenseState.UnAuthorized
                } else {
                    ExpenseState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        } catch (throwable: Throwable) {
            ExpenseState.ServerError(errorHandler.getError(throwable))
        }
    }
}