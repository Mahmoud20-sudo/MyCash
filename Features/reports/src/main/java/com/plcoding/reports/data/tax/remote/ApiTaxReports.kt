package com.plcoding.reports.data.tax.remote

import com.plcoding.reports.data.tax.model.TaxReportsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiTaxReports {
    @GET("sales/taxReport/cal_report")
    suspend fun getTaxReports(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page : Int?,
        @Query("date_from") dateFrom: String?,
        @Query("date_to") dateTo: String?,
        @Query("dateType") dateType: Int?,
    ): Response<TaxReportsDTO>

}