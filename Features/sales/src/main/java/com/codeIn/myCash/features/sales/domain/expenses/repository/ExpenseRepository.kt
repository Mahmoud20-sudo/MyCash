package com.codeIn.myCash.features.sales.domain.expenses.repository

import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import java.io.File

interface ExpenseRepository {

    suspend fun getExpenses(type : String? , limit : String? ,search : String? , date : String?) : ExpenseState

    suspend fun getSingleExpense(expenseId : String?): ExpenseState

    suspend fun deleteExpense(expenseId : String?): ExpenseState

    suspend fun createExpense(statement: String? , amount: String? , date : String? ,
                             note: String? , additionalInfo : String? , file : File? ,
                              tax : String?): ExpenseState

    suspend fun updateExpense(expenseId: String?, statement: String?, amount: String?, date: String?,
                              note: String?, additionalInfo: String?, file: File? , tax : String?): ExpenseState
}