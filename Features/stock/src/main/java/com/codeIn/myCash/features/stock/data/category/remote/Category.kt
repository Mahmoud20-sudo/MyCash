package com.codeIn.myCash.features.stock.data.category.remote

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoriesDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Category {

    @GET("stock/category/get")
    suspend fun getCategories(@Header("lang") lang : String?,
                              @Header("Authorization") authorization:String?,
                              @Query("parents") parents : String? ,
                              @Query("parent_id") parentId : String?,
                              @Query("cat_id") categoryId : String?
                              ): Response<CategoriesDTO>
}