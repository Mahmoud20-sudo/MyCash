package com.codeIn.myCash.features.user.data.edit_profile.remote

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.EditCodeDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.MyInfoDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.UpdateProfileDTO
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
import java.io.File

interface ApiEditProfile {
    @GET("client/auth/my_info")
    suspend fun profile(@Header("lang") lang : String?,
                        @Header("Authorization") authorization:String?): Response<UserDTO>
    @FormUrlEncoded
    @POST("client/profile/update_phone")
    suspend fun updatePhone(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("phone") phone:String?,
        @Field("code") code:Int?, ):Response<UpdateProfileDTO>
    @FormUrlEncoded
    @POST("client/profile/update_email")
    suspend fun updateEmail(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("email") email:String?,
        @Field("code") code:Int?, ):Response<UpdateProfileDTO>
    @FormUrlEncoded
    @POST("client/profile/edit_code")
    suspend fun editCode(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("phone") phone:String?,
        @Field("type") type:Int?,
        @Field("email") email:String?,
        ):Response<EditCodeDTO>

    @Multipart
    @POST("client/profile/update_profile")
    suspend fun updateProfile(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Part logo: MultipartBody.Part,
        @Query("commercialRecordName") commercialRecordName:String?,
        @Query("tax") tax:String?,
        @Query("taxRecord") taxRecord:String?,
        @Query("commercialRecord") commercialRecord:String?
        ):Response<UserDTO>



    @FormUrlEncoded
    @POST("client/profile/update_profile")
    suspend fun updateProfileWithoutImGW(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("commercialRecord") commercialRecord:String?,
        @Field("tax") tax:String?,
        @Field("taxRecord") taxRecord:String?,
        @Field("commercialRecordName") commercialRecordName:String?
        ):Response<UserDTO>


}