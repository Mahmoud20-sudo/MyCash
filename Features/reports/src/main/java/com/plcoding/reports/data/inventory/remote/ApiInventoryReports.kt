package com.plcoding.reports.data.inventory.remote

import com.plcoding.reports.data.inventory.model.InventoryReportsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInventoryReports {
    @GET("stock/inventory/get")
    suspend fun getInventory(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page : Int?,
        @Query("dateType") type: Int? = null,//1=>day , 2=>week , 3=>month , 4=>year
        @Query("product_id") productId: Int? = null,
    ): Response<InventoryReportsDTO>

}