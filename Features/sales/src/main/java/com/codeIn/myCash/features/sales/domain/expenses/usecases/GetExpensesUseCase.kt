package com.codeIn.myCash.features.sales.domain.expenses.usecases

import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.sales.domain.expenses.repository.ExpenseRepository
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(private val repository: ExpenseRepository){
    suspend operator fun invoke(type : String?, limit : String? ,search : String? , date : String?) : ExpenseState {
        return repository.getExpenses(type,limit , search , date)
    }
}