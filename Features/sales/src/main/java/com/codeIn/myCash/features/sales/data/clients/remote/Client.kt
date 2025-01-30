package com.codeIn.myCash.features.sales.data.clients.remote

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientsDTO
import com.codeIn.myCash.features.sales.data.clients.remote.response.SingleClientDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Client {

    @GET("sales/client/get")
    suspend fun getClients(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("phone") phone: String?,
        @Query("search_text") searchText: String?,
        @Query("payment_status") paymentStatus: String?,
        @Query("type") type: String?,
        @Query("limit") limit: String?,
    ): Response<ClientsDTO>

    @GET("sales/client/single")
    suspend fun getSingleClient(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("client_id") clientId: String?,
        @Query("type") type: String?,
    ): Response<SingleClientDTO>

    @DELETE("sales/client/delete")
    suspend fun deleteClient(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("client_id") clientId: String?,
        @Query("type") type: String?,
    ): Response<BaseResponseDTO>

    @FormUrlEncoded
    @POST("sales/client/create")
    suspend fun createClient(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String?,
        @Field("country_id") countryId: String?,
        @Field("address") address: String?,
        @Field("taxRecord") taxRecord: String?,
        @Field("commercialRecord") commercialRecord: String?,
        @Field("notes") notes: String?,
        @Field("type") type: Int,
    ): Response<SingleClientDTO>


    @FormUrlEncoded
    @POST("sales/client/update")
    suspend fun updateClient(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("client_id") clientId: Int?,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("country_id") countryId: String?,
        @Field("phone") phone: String?,
        @Field("address") address: String?,
        @Field("taxRecord") taxRecord: String?,
        @Field("commercialRecord") commercialRecord: String?,
        @Field("notes") notes: String?,
        @Field("type") type: Int,
    ): Response<SingleClientDTO>
}