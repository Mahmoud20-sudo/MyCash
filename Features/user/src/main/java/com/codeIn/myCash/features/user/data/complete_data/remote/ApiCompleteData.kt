package com.codeIn.myCash.features.user.data.complete_data.remote


import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
interface ApiCompleteData {

    @FormUrlEncoded
    @POST("client/account_info/complete_data")
    suspend fun completeData(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("commercialRecord") commercialRecord: String?,
        @Field("tax") tax: String?,
        @Field("taxRecord") taxRecord: String?,
        @Field("commercialRecordName") commercialRecordName: String?,
        @Field("branch_name") branchName: String?,
        @Field("branch_address") branchAddress: String?,
    ): Response<UserDTO>

    @POST("client/auth/logout")
    suspend fun logout(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?
    ): Response<BaseResponseDTO>

}