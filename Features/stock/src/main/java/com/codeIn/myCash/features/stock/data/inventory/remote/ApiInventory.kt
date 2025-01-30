package com.codeIn.stock.inventory.data.remote

import com.codeIn.stock.inventory.data.model.response.InventoryDTO
import com.codeIn.stock.inventory.data.model.response.SalesReportDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInventory {

    @GET("stock/inventory/get")
    suspend fun getInventory(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("type") type: Int
    ): Response<InventoryDTO>

    @GET("stock/inventory/single")
    suspend fun getSingleInventory(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("inventory_id") inventoryId: Int
    ): Response<InventoryDTO>

    @FormUrlEncoded
    @POST("stock/inventory/create")
    suspend fun createInventory(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("product_id") productID: Int,
        @Field("allQuantity") allQuantity: Int,
        @Field("quantity") quantity: Int,
        @Field("damageQuantity") damageQuantity: Int,
    ): Response<InventoryDTO>
    @FormUrlEncoded
    @POST("stock/inventory/update")
    suspend fun updateInventory(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("inventory_id") inventoryID: Int,
        @Field("allQuantity") allQuantity: Int,
        @Field("quantity") quantity: Int,
        @Field("damageQuantity") damageQuantity: Int,
        @Field("product_id") productID: Int,
    ): Response<InventoryDTO>
    @FormUrlEncoded
    @POST("stock/inventory/delete")
    suspend fun deleteInventory(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("inventory_id") inventoryID: Int,
    ): Response<InventoryDTO>

    @GET("sales/report/get")
    suspend fun getSalesReport(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
    ): Response<SalesReportDTO>

    @GET("sales/report/single")
    suspend fun salesDetails(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("report_id") reportID :Int,
    ): Response<SalesReportDTO>


}