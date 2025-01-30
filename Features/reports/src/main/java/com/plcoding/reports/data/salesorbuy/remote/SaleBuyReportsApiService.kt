package com.plcoding.reports.data.salesorbuy.remote

import com.plcoding.reports.data.salesorbuy.model.ReportsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SaleBuyReportsApiService {
    @GET("sales/report/cal_report")
    suspend fun getSaleInvoices(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int?,
        @Query("date_from") dateFrom: String?,
        @Query("date_to") dateTo: String?,
        @Query("branch_id") branchId: Int,
    ): Response<ReportsDTO>

}