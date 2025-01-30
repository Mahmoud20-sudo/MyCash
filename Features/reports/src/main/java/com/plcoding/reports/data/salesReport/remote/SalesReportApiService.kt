package com.plcoding.reports.data.salesReport.remote

import com.plcoding.reports.data.salesReport.model.response.SalesReportResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SalesReportApiService {

    @GET("sales/report/cal_sales_report")
    suspend fun getSalesReport(
        @Header("lang") lang: String?,
        @Header("Authorization") token: String?,
        @Query("start_date") startDate: String?,
        @Query("end_date") endDate: String?,
        @Query("interval") interval: String?,
        @Query("branch_id") branchId: Int?,
    ): Response<SalesReportResponse>

}