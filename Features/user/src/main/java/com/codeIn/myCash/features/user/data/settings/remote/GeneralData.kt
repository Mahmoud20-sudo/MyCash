package com.codeIn.myCash.features.user.data.settings.remote

import com.codeIn.myCash.features.user.data.settings.remote.response.device.DevicesDTO
import com.codeIn.myCash.features.user.data.settings.remote.response.settings.SettingsDTO
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.PackagesDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GeneralData {

    @GET("client/general_data/get_devices")
    suspend fun getDevices(@Header("lang") lang : String?,
                           @Query("country_id") countryId : String): Response<DevicesDTO>

    @GET("client/general_data/get_setting")
    suspend fun getSettings(@Header("lang") lang : String?): Response<SettingsDTO>


    @GET("client/general_data/get_packages")
    suspend fun getPackages(@Header("lang") lang : String?,
                            @Query("country_id") countryId : String?) : Response<PackagesDTO>
}