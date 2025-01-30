package com.plcoding.reports.data.expense.model

import com.codeIn.common.domain.ErrorEntity

sealed class ExpenseState {
    data object Idle : ExpenseState()
    data object Loading : ExpenseState()
    data class Success(val data: ExpensesData?) : ExpenseState()
    data class ServerError(val error: ErrorEntity) : ExpenseState()
    data class StateError(val message: String?) : ExpenseState()
    data object UnAuthorized : ExpenseState()
}