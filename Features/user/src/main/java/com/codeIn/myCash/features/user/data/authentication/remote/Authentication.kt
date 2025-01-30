package com.codeIn.myCash.features.user.data.authentication.remote

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountCodeDTO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Authentication {

    @FormUrlEncoded
    @POST("client/auth/login")
    suspend fun login(@Header("lang") lang : String?,
                      @Field("phone") phone : String?,
                      @Field("password") password : String,
                      @Field("isPhone") isPhone : String?,// 1 >> in case phone
                      @Field("country_id") countryId : String?,
                      @Field("deviceToken") deviceToken : String?,
                      ): Response<UserDTO>

    @FormUrlEncoded
    @POST("client/auth/register")
    suspend fun register(@Header("lang") lang : String?,
                         @Field("phone") phone : String,
                         @Field("password") password : String,
                         @Field("email") email : String?,
                         @Field("country_id") countryId : String,
                         @Field("package_id") packageId : String?,): Response<UserDTO>

    @GET("client/auth/my_info")
    suspend fun profile(@Header("lang") lang : String?,
                        @Header("Authorization") authorization:String?): Response<UserDTO>


    @FormUrlEncoded
    @POST("client/auth/forget_password")
    suspend fun forgetPassword(@Header("lang") lang : String?,
                               @Field("key") key : String ,
                               @Field("type") type : String ,//1=>phone , 2=>email
                               @Field("country_id") countryId : String?): Response<UserDTO>

    @FormUrlEncoded
    @POST("client/auth/reset_password")
    suspend fun resetPassword(@Header("lang") lang : String? ,
                              @Header("Authorization") authorization : String? ,
                              @Field("password") password : String?
                              ): Response<UserDTO>

    @FormUrlEncoded
    @POST("client/auth/save_firebase")
    suspend fun saveFirebase (@Header("lang") lang : String ,
                              @Header("Authorization") authorization : String ,
                              @Field("firebase") firebase : String
                              ): Response<BaseResponseDTO>

    @FormUrlEncoded
    @POST("client/auth/check_code")
    suspend fun checkCode(@Header("lang") lang : String? ,
                          @Field("code") code : String?,
                          @Field("phone") phone : String? ,
                          @Field("country_id") countryId : String? ,
                          @Field("type") type : String , //1=>phone , 2 =>email
                          @Field("email") email : String? ,
                          @Field("active") active : String? //send 1 in check active code
                          ): Response<UserDTO>

    @FormUrlEncoded
    @POST("client/auth/resend_code")
    suspend fun resendCode(@Header("lang") lang : String? ,
                           @Field("phone") phone : String? ,
                           @Field("country_id") countryId : String? ,
                           @Field("email") email : String? ,
                           @Field("type") type : String?
                           ): Response<UserDTO>


    @FormUrlEncoded
    @POST("client/discount/check_discount_code")
    suspend fun checkCodeDiscount(@Header("lang") lang : String? ,
                                  @Field("code") code: String?,
                                  @Field("country_id") countryId : String?): Response<DiscountCodeDTO>

    @FormUrlEncoded
    @POST("client/payment/get_payment_link")
    suspend fun getPaymentLink(@Header("lang") lang : String?,
                               @Header("Authorization") authorization : String?,
                               @Field("influencer_id") influenceId : String?,
                               @Field("device_country_id") deviceCountryId : String?,
                               @Field("package_id") packageId : String?,
                               ): Response<BaseResponseDTO>

    @FormUrlEncoded
    @POST("client/payment/get_subscription_payment_link")
    suspend fun getRenewPaymentLink(@Header("lang") lang : String?,
                               @Header("Authorization") authorization : String?,
                               @Field("influencer_id") influenceId : String?,
                               @Field("device_country_id") deviceCountryId : String?,
                               @Field("package_id") packageId : String?,
                               ): Response<BaseResponseDTO>

    @FormUrlEncoded
    @POST("client/account_info/complete_data")
    suspend fun enableFeaturesOfSettings(@Header("lang") lang : String? ,
                                         @Header("Authorization") authorization : String? ,
                                         @Field("notification") notification : String?,
                                         @Field("drafts") drafts : String? ,
                                         @Field("quickInvoice") quickInvoice : String?,
                                         ) : Response<UserDTO>


}