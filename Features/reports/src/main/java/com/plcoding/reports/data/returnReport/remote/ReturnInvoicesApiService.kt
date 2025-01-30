package com.plcoding.reports.data.returnReport.remote

import com.plcoding.reports.data.returnReport.model.response.ReturnInvoicesReportResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ReturnInvoicesApiService {

    @GET("sales/report/cal_return_report")
    suspend fun getReturnInvoices(
        @Header("lang") lang: String?,
        @Header("Authorization") token: String?,
        @Query("page") page: Int?,
        @Query("start_date") startDate: String?,
        @Query("end_date") endDate: String?,
        @Query("branch_id") branchId: Int,
    ): Response<ReturnInvoicesReportResponse>
}