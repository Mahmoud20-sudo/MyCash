package com.plcoding.reports.data.expense.remote

import com.plcoding.reports.data.expense.model.ExpenseReportsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiExpenseReports {
    @GET("sales/expenseReport/cal_report")
    suspend fun getExpenses(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page : Int?,
        @Query("date_from") dateFrom : String?,
        @Query("date_to") dateTo : String?,
        @Query("dateType") dateType : Int?,
        ): Response<ExpenseReportsDTO>

}
