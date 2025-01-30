package com.codeIn.myCash.features.sales.data.expenses.remote

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.sales.data.expenses.remote.response.DetailsExpenseDTO
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpensesDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface Expenses {

    @GET("sales/expense/get")
    suspend fun getExpenses(@Header("lang") lang : String?,
                            @Header("Authorization") authorization:String?,
                            @Query("dateType") type : String?,
                            @Query("limit") limit : String? ,
                            @Query("search_text") searchText : String? ,
                            @Query("date") date : String?,
                            ): Response<ExpensesDTO>


    @FormUrlEncoded
    @POST("sales/expense/create")
    suspend fun createExpenseWithoutFile(@Header("lang") lang : String?,
                                         @Header("Authorization") authorization:String?,
                                         @Field("statement") statement: String? ,
                                         @Field("amount") amount: String? ,
                                         @Field("date") date: String? ,
                                         @Field("note") note: String? ,
                                         @Field("additional_info") additionalInfo: String? ,
                                         @Field("tax") tax : String?,
                                         ): Response<DetailsExpenseDTO>

    @Multipart
    @POST("sales/expense/create")
    suspend fun createExpenseWithFile(@Header("lang") lang : String?,
                                      @Header("Authorization") authorization:String?,
                                      @Query("statement") statement: String? ,
                                      @Query("amount") amount: String? ,
                                      @Query("date") date: String? ,
                                      @Query("note") note: String? ,
                                      @Query("additional_info") additionalInfo: String? ,
                                      @Query("tax") tax : String?,
                                      @Part file : MultipartBody.Part
    ): Response<DetailsExpenseDTO>


    @FormUrlEncoded
    @POST("sales/expense/update")
    suspend fun updateExpenseWithoutFile(@Header("lang") lang : String?,
                                         @Header("Authorization") authorization:String?,
                                         @Field("expense_id") expenseId: String? ,
                                         @Field("statement") statement: String? ,
                                         @Field("amount") amount: String? ,
                                         @Field("date") date: String? ,
                                         @Field("note") note: String? ,
                                         @Field("additional_info") additionalInfo: String? ,
                                         @Field("tax") tax : String?,
    ): Response<DetailsExpenseDTO>

    @Multipart
    @POST("sales/expense/update")
    suspend fun updateExpenseWithFile(@Header("lang") lang : String?,
                                      @Header("Authorization") authorization:String?,
                                      @Query("expense_id") expenseId: String? ,
                                      @Query("statement") statement: String? ,
                                      @Query("amount") amount: String? ,
                                      @Query("date") date: String? ,
                                      @Query("note") note: String? ,
                                      @Query("additional_info") additionalInfo: String? ,
                                      @Part file : MultipartBody.Part ,
                                      @Query("tax") tax : String?,
    ): Response<DetailsExpenseDTO>


    @GET("sales/expense/single")
    suspend fun getSingleExpense(@Header("lang") lang : String?,
                                 @Header("Authorization") authorization:String?,
                                 @Query("expense_id") expenseId: String? ,
                                 ):Response<DetailsExpenseDTO>

    @DELETE("sales/expense/delete")
    suspend fun deleteExpense(@Header("lang") lang : String?,
                                 @Header("Authorization") authorization:String?,
                                 @Query("expense_id") expenseId: String? ,
    ):Response<BaseResponseDTO>

}