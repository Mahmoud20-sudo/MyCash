package com.codeIn.myCash.features.stock.data.product.remote

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.stock.data.product.remote.response.DetailsProductDTO
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface Product {

    @Multipart
    @POST("stock/products/create")
    suspend fun createProductWithImage(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("name") name: String?,
        @Query("desc") desc: String?,
        @Query("barCode") barCode: String?,
        @Query("quantity") quantity: String?,
        @Query("taxAvailable") taxAvailable: Int,
        @Query("taxType") taxType: Int,
        @Query("hasDiscount") hasDiscount: Int,
        @Query("discountType") discountType: Int,
        @Query("discount") discount: String?,
        @Query("price") price: String?,
        @Query("category") category: String?,
        @Query("parentCat_id") parentCatId: String?,
        @Query("cat_id") catId: String?,
        @Query("buyPrice") buyPrice: String? = null,
        @Part image: MultipartBody.Part,
        @Query("buyTaxAvailable") buyTaxAvailable: Int? = null,
        @Query("buyTaxType") buyTaxType: Int? = null,
        @Query("branch_id") branch: String?
    ): Response<DetailsProductDTO>

    @FormUrlEncoded
    @POST("stock/products/create")
    suspend fun createProductWithoutImage(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("name") name: String?,
        @Field("desc") desc: String?,
        @Field("barCode") barCode: String?,
        @Field("quantity") quantity: String?,
        @Field("hasDiscount") hasDiscount: Int,
        @Field("discountType") discountType: Int,
        @Field("discount") discount: String?,
        @Field("price") price: String?,
        @Field("taxAvailable") taxAvailable: Int,
        @Field("taxType") taxType: Int,
        @Field("category") category: String?,
        @Field("parentCat_id") parentCatId: String?,
        @Field("cat_id") catId: String?,
        @Query("buyPrice") buyPrice: String? = null,
        @Field("buyTaxAvailable") buyTaxAvailable: Int? = null,
        @Field("buyTaxType") buyTaxType: Int? = null,
        @Query("branch_id") branch: String?
    ): Response<DetailsProductDTO>


    @GET("stock/products/get")
    suspend fun getProducts(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("sort") sort: String?,
        @Query("cat_id") categoryId: String?,
        @Query("searchText") searchText: String?,
        @Query("parentCat_id") parentCategoryId: String?,
        @Query("discountType") discountType: String?,
        @Query("date") date: String?,
        @Query("limit") limit: String?,
    ): Response<ProductsDTO>


    @FormUrlEncoded
    @POST("stock/products/delete")
    suspend fun deleteProduct(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("product_id") productId: String?
    ): Response<BaseResponseDTO>


    @Multipart
    @POST("stock/products/update")
    suspend fun updateProductWithImage(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("product_id") productId: String?,
        @Query("name") name: String?,
        @Query("desc") desc: String?,
        @Query("barCode") barCode: String?,
        @Query("quantity") quantity: String?,
        @Query("taxAvailable") taxAvailable: Int,
        @Query("taxType") taxType: Int,
        @Query("hasDiscount") hasDiscount: Int,
        @Query("discountType") discountType: Int,
        @Query("discount") discount: String?,
        @Query("price") price: String?,
        @Query("category") category: String?,
        @Query("parentCat_id") parentCatId: String?,
        @Query("cat_id") catId: String?,
        @Query("buyPrice") buyPrice: String? = null,
        @Part image: MultipartBody.Part,
        @Query("buyTaxAvailable") buyTaxAvailable: Int? = null,
        @Query("buyTaxType") buyTaxType: Int? = null,
        @Query("branch_id") branch: String?
    ): Response<DetailsProductDTO>

    @FormUrlEncoded
    @POST("stock/products/update")
    suspend fun updateProductWithoutImage(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Field("product_id") productId: String?,
        @Field("name") name: String?,
        @Field("desc") desc: String?,
        @Field("barCode") barCode: String?,
        @Field("quantity") quantity: String?,
        @Field("hasDiscount") hasDiscount: Int,
        @Field("discountType") discountType: Int,
        @Field("discount") discount: String?,
        @Field("price") price: String?,
        @Field("taxAvailable") taxAvailable: Int,
        @Field("taxType") taxType: Int,
        @Field("category") category: String?,
        @Field("parentCat_id") parentCatId: String?,
        @Field("cat_id") catId: String?,
        @Field("buyPrice") buyPrice: String? = null,
        @Field("buyTaxAvailable") buyTaxAvailable: Int? = null,
        @Field("buyTaxType") buyTaxType: Int? = null,
        @Query("branch_id") branch: String?
    ): Response<DetailsProductDTO>


    @GET("stock/products/single")
    suspend fun getSingleProduct(
        @Header("lang") lang: String?,
        @Header("Authorization") authorization: String?,
        @Query("product_id") productId: String?,
    ): Response<DetailsProductDTO>

}