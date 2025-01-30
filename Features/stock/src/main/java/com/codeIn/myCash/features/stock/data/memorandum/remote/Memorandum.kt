package com.codeIn.myCash.features.stock.data.memorandum.remote

import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumDTO
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumsDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Memorandum {
    @FormUrlEncoded
    @POST("sales/invoiceNotification/make_invoice_notification")
    suspend fun makeMemorandum(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("invoice_id") invoiceId : String? ,
        @Field("type") type : String? ,
        @Field("cash") cash : String? ,
        @Field("visa") visa : String? ,
        @Field("products") products : String? ,
        @Field("paymentType") paymentType : String?): Response<MemorandumDTO>

    @GET("sales/invoiceNotification/get")
    suspend fun getMemorandums(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Query("invoice_id") invoiceId : String? ,
        @Query("type") type : String? ,
        @Query("limit") limit : String? ,
    ):Response<MemorandumsDTO>

    @GET("sales/invoiceNotification/single")
    suspend fun getSingleMemorandum(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Query("invoiceNotification_id") memorandumId : String?
    ):Response<MemorandumDTO>
}