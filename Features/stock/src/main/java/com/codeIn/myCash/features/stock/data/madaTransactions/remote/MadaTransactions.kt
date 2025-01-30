package com.codeIn.myCash.features.stock.data.madaTransactions.remote

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionsDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MadaTransactions {

    @FormUrlEncoded
    @POST("sales/transaction/create")
    suspend fun createTransactions(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("amount") amount : String? ,
        @Field("runRefund") runRefund : String? ,
        @Field("codeRefund") codeRefund : String? ,
        @Field("dateRefund") dateRefund : String? ,
        @Field("type") type : String? ,
    ): Response<BaseResponseDTO>

    @GET("sales/transaction/get")
    suspend fun getTransactions(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
    ): Response<TransactionsDTO>


}