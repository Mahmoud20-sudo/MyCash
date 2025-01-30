package com.codeIn.myCash.features.sales.domain.expenses.usecases

import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.sales.domain.expenses.repository.ExpenseRepository
import java.io.File
import javax.inject.Inject

class UpdateExpenseUseCase @Inject constructor(private val repository: ExpenseRepository){
    suspend operator fun invoke(expenseId : String? , statement: String? , amount: String? , date : String? ,
                                note: String? , additionalInfo : String? , file : File? , tax : String?) : ExpenseState {
        return repository.updateExpense(expenseId, statement, amount, date, note, additionalInfo, file , tax )
    }
}