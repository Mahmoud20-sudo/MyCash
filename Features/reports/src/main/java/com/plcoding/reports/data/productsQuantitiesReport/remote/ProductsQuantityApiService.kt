package com.plcoding.reports.data.productsQuantitiesReport.remote

import com.plcoding.reports.data.productsQuantitiesReport.model.response.ProductsQuantityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductsQuantityApiService {

    @GET("sales/report/cal_products_quantities_report")
    suspend fun getProductsQuantityReport(
        @Header("lang") lang: String?,
        @Header("Authorization") token: String?,
        @Query("page") page: Int?,

        @Query("category") productStatusId: Int?,
        @Query("comparison_option") comparisonOption: Int?,  // -> 0 for less than and 1 for equal to
        @Query("quantities") quantities: Int?,
        @Query("branch_id") branchId: Int,
    ): Response<ProductsQuantityResponse>
}