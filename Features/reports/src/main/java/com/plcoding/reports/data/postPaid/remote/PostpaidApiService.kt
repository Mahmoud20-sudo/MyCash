package com.plcoding.reports.data.postPaid.remote

import com.plcoding.reports.data.postPaid.model.response.PostpaidResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PostpaidApiService {

    @GET("sales/report/cal_postpaid_report")
    suspend fun getPostpaidReports(
        @Header("lang") lang: String?,
        @Header("Authorization") token: String?,

        @Query("start_date") startDate: String?,
        @Query("end_date") endDate: String?,
        @Query("invoice_number") invoiceNumber: Int?,
        @Query("receipt_number") receiptNumber: Int?,
        @Query("receipt_state_id") receiptStatusId: Int,
        @Query("branch_id") branchId: Int,
    ): Response<PostpaidResponse>
}