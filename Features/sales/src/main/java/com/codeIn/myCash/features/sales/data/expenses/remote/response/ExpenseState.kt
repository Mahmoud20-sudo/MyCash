package com.codeIn.myCash.features.sales.data.expenses.remote.response

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class ExpenseState{

    data object Idle : ExpenseState()

    data object Loading : ExpenseState()

    data class SuccessShowExpenses(val data : ExpensesData?) : ExpenseState()

    data class SuccessShowSingleExpense(val data : ExpenseModel?) : ExpenseState()

    data class SuccessDeleteExpense(val message: String?) : ExpenseState()

    data class ServerError(val error : ErrorEntity) : ExpenseState()

    data class StateError(val message : String?) : ExpenseState()

    data class InputError(val inputError : InvalidInput) : ExpenseState()
    data object UnAuthorized: ExpenseState()
}