package com.codeIn.myCash.features.sales.data.branch.remote

import com.codeIn.myCash.features.sales.data.branch.model.response.DeleteDTO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.sales.branch.data.model.response.CreateBranchesDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiBranches {

    @GET("sales/branch/get")
    suspend fun getBranches(
        @Header("Authorization") authorization: String?,
        @Query("search_text") searchText : String? ,
        @Query("limit") limit : String? = "100"
    ): Response<BranchesDTO>

    @GET("sales/branch/single")
    suspend fun getBranchDetails(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("branch_id") branchID: Int
    ): Response<BranchesDTO>

    @FormUrlEncoded
    @POST("sales/branch/create")
    suspend fun createBranches(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization:String?,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("isMain") isMain: Int,
        @Field("employee_id") employee_id: Int,
        @Field("status") status: Int,
        @Field("city")city:String,
        @Field("additional_info")additionalInfo: String,
        @Field("phone")phone:String?,
    ):Response<CreateBranchesDTO>

    @FormUrlEncoded
    @POST("sales/branch/delete")
    suspend fun deleteBranches(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization:String?,
        @Field("all") all: Int? = null
    ):Response<DeleteDTO>

    @FormUrlEncoded
    @POST("sales/branch/delete")
    suspend fun deleteBranchesID(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization:String?,
        @Field("branch_id") branchId: Int? = null,
    ):Response<DeleteDTO>

    @FormUrlEncoded
    @POST("sales/branch/update")
    suspend fun updateBranch(
        @Header("lang") lang: String?,
        @Header("Authorization")authorization :String?,
        @Field("name")name:String,
        @Field("isMain") isMain: Int,
        @Field("address") address: String,
        @Field("branch_id")branchID:Int,
        @Field("status") status: Int,
        @Field("additional_info")additionalInfo: String,
        @Field("city")city:String,
        @Field("phone")phone:String?,
    ): Response<CreateBranchesDTO>


}