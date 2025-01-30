package com.codeIn.myCash.features.user.data.shift.remote

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Shift {

    @GET("client/shift/current_shift")
    suspend fun currentShift(@Header("lang") lang : String?,
                             @Header("Authorization") authorization:String? ): Response<ShiftDTO>

    @FormUrlEncoded
    @POST("client/shift/open_shift")
    suspend fun startShift(@Header("lang") lang : String?,
                           @Header("Authorization") authorization:String?,
                           @Field("startCash") startCash : String?,
                           @Field("startVisa") startVisa : String? ,
                           @Field("differentInCash") differentInCash : String? ,
                           @Field("differentInVisa") differentInVisa : String? ,
                           ): Response<ShiftDTO>
    @FormUrlEncoded
    @POST("client/auth/logout")
    suspend fun logout(@Header("lang") lang : String?,
                       @Header("Authorization") authorization:String?,
                       @Field("endCash") endCash : String? ,
                       @Field("endVisa") endVisa : String? , ):Response<BaseResponseDTO>


}