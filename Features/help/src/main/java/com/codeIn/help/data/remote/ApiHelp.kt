package com.codeIn.help.data.remote

import com.codeIn.help.data.model.response.GetHelpDTO
import com.codeIn.help.data.model.response.HowWorkDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiHelp {

    @GET("client/general_data/get_setting")
    suspend fun getSetting(
        @Header("lang") lang: String?
    ):Response<GetHelpDTO>

    @GET("client/general_data/how_work")
    suspend fun getHowWork(
        @Header("lang") lang: String?
    ):Response<HowWorkDTO>
}