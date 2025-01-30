package com.codeIn.myCash.features.sales.data.expenses.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.data.expenses.remote.Expenses
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.sales.domain.expenses.repository.ExpenseRepository
import com.codeIn.myCash.features.sales.domain.expenses.usecases.CreateExpenseValidationUseCase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor (private var expenses: Expenses,
                                                 private val prefs : SharedPrefsModule,
                                                 private val errorHandler: ErrorHandlerImpl,
                                                 private val createExpenseValidationUseCase: CreateExpenseValidationUseCase
) : ExpenseRepository {
    override suspend fun getExpenses(type: String?, limit: String? , search : String? , date : String?): ExpenseState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            expenses.getExpenses(lang, token , type , limit , search , date).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        ExpenseState.SuccessShowExpenses(response.body()?.data)
                    } else {
                        ExpenseState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    ExpenseState.UnAuthorized
                } else {
                    ExpenseState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        } catch (throwable: Throwable) {
            ExpenseState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getSingleExpense(expenseId: String?): ExpenseState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            expenses.getSingleExpense(lang, token , expenseId).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        ExpenseState.SuccessShowSingleExpense(response.body()?.data)
                    } else {
                        ExpenseState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401){
                    ExpenseState.UnAuthorized
                }else {
                    ExpenseState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        } catch (throwable: Throwable) {
            ExpenseState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun deleteExpense(expenseId: String?): ExpenseState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            expenses.deleteExpense(lang, token , expenseId).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        ExpenseState.SuccessDeleteExpense(response.body()?.message)
                    } else {
                        ExpenseState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401){
                    ExpenseState.UnAuthorized
                }else {
                    ExpenseState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        } catch (throwable: Throwable) {
            ExpenseState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun createExpense(
        statement: String?,
        amount: String?,
        date: String?,
        note: String?,
        additionalInfo: String?,
        file: File? ,
        tax : String?
    ): ExpenseState {
        return try {
            createExpenseValidationUseCase.invoke(statement, amount, date)
                .let { if (it != InvalidInput.NONE) return ExpenseState.InputError(it) }
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            if (file != null ){
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                expenses.createExpenseWithFile(
                    lang, token, statement , amount, date, note, additionalInfo , tax , body ,
                ).let { response ->
                    if (response.isSuccessful) {
                        if (response.body()?.status == 1) {
                            ExpenseState.SuccessShowSingleExpense(response.body()?.data)
                        } else {
                            ExpenseState.StateError(response.body()?.message)
                        }
                    } else if (response.code() == 401){
                        ExpenseState.UnAuthorized
                    }else {
                        ExpenseState.ServerError(errorHandler.invoke(response.code()))
                    }
                }
            }
            else {
                expenses.createExpenseWithoutFile(
                    lang, token, statement , amount, date, note, additionalInfo , tax
                ).let { response ->
                    if (response.isSuccessful) {
                        if (response.body()?.status == 1) {
                            ExpenseState.SuccessShowSingleExpense(response.body()?.data)
                        } else {
                            ExpenseState.StateError(response.body()?.message)
                        }
                    } else if (response.code() == 401){
                        ExpenseState.UnAuthorized
                    }else {
                        ExpenseState.ServerError(errorHandler.invoke(response.code()))
                    }
                }
            }

        } catch (throwable: Throwable) {
            ExpenseState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun updateExpense(
        expenseId: String?,
        statement: String?,
        amount: String?,
        date: String?,
        note: String?,
        additionalInfo: String?,
        file: File? ,
        tax : String?
    ): ExpenseState {
        return try {
            createExpenseValidationUseCase.invoke(statement, amount, date)
                .let { if (it != InvalidInput.NONE) return ExpenseState.InputError(it) }
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            if (file != null ){
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                expenses.updateExpenseWithFile(
                    lang, token, expenseId ,  statement , amount, date, note, additionalInfo , body , tax
                ).let { response ->
                    if (response.isSuccessful) {
                        if (response.body()?.status == 1) {
                            ExpenseState.SuccessShowSingleExpense(response.body()?.data)
                        } else {
                            ExpenseState.StateError(response.body()?.message)
                        }
                    } else if (response.code() == 401){
                        ExpenseState.UnAuthorized
                    }else {
                        ExpenseState.ServerError(errorHandler.invoke(response.code()))
                    }
                }


            }
            else {
                expenses.updateExpenseWithoutFile(
                    lang, token, expenseId ,  statement , amount, date, note, additionalInfo , tax
                ).let { response ->
                    if (response.isSuccessful) {
                        if (response.body()?.status == 1) {
                            ExpenseState.SuccessShowSingleExpense(response.body()?.data)
                        } else {
                            ExpenseState.StateError(response.body()?.message)
                        }
                    } else if (response.code() == 401){
                        ExpenseState.UnAuthorized
                    }else {
                        ExpenseState.ServerError(errorHandler.invoke(response.code()))
                    }
                }
            }

        } catch (throwable: Throwable) {
            ExpenseState.ServerError(errorHandler.getError(throwable))
        }
    }


}