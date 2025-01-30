package com.codeIn.myCash.features.user.data.users.remote

import com.codeIn.myCash.features.user.data.users.model.response.CreateUserDTO
import com.codeIn.myCash.features.user.data.users.model.response.DeleteDTO
import com.codeIn.myCash.features.user.data.users.model.response.GetUsersDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiUsers {
    @GET("client/employee/get")
    suspend fun getUsers(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("search_text") searchText : String?
    ): Response<GetUsersDTO>

    @GET("client/employee/get")
    suspend fun getRefresh(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
    ): Response<GetUsersDTO>

    @FormUrlEncoded
    @POST("client/employee/create")
    suspend fun createUsers(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String?,
        @Field("password") password: String,
        @Field("status") status: Int,
        @Field("type") type: String?,
        @Field("branch_id") branchId : String? ,
    ): Response<CreateUserDTO>

    @FormUrlEncoded
    @POST("client/employee/update")
    suspend fun updateUsers(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String?,
        @Field("password") password: String,
        @Field("note") note: String,
        @Field("employee_id") employeeID: Int,
        @Field("branch_id") branchId : String? ,
        @Field("type") type: String?,
    ): Response<GetUsersDTO>

    @GET("client/employee/single")
    suspend fun getUserDetails(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("employee_id") employeeId: Int
    ): Response<GetUsersDTO>
    @FormUrlEncoded
    @POST("client/employee/delete")
    suspend fun deleteUser(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("employee_id") employeeId: Int
    ): Response<DeleteDTO>

    @FormUrlEncoded
    @POST("client/employee/delete")
    suspend fun deleteAllUser(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("all") all: Int
    ): Response<DeleteDTO>
}
