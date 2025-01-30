package com.codeIn.myCash.features.sales.domain.expenses.usecases

import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.sales.domain.expenses.repository.ExpenseRepository
import javax.inject.Inject

class GetSingleExpenseUseCase @Inject constructor(private val repository: ExpenseRepository){
    suspend operator fun invoke(expenseId : String?) : ExpenseState {
        return repository.getSingleExpense(expenseId)
    }
}