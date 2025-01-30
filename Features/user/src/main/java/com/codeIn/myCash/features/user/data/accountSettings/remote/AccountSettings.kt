package com.codeIn.myCash.features.user.data.accountSettings.remote

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoicesSettingsDTO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountSettings {


    @FormUrlEncoded
    @POST("client/invoice_setting/update_invoice_setting")
    suspend fun updateInvoiceSettings(@Header("lang") lang : String? ,
                                      @Header("Authorization") authorization : String? ,
                                      @Field("name") name : String? ,
                                      @Field("commercialRecord") commercialRecord : String? ,
                                      @Field("taxRecord") taxRecord : String? ,
                                      @Field("tax") tax : String?): Response<InvoicesSettingsDTO>

    @GET("client/invoice_setting/get_invoice_setting")
    suspend fun invoiceSettings(@Header("lang") lang : String? ,
                                @Header("Authorization") authorization : String? ):Response<InvoicesSettingsDTO>

    @FormUrlEncoded
    @POST("client/invoice_setting/update_invoice_setting_value")
    suspend fun updateInvoiceSettingValue(@Header("lang") lang : String? ,
                                          @Header("Authorization") authorization : String? ,
                                          @Field("productDesc") productDesc : String? ,
                                          @Field("footerText") footerText : String? ,
                                          @Field("client") client : String? ,
                                          @Field("cashier") cashier : String? ,
                                          @Field("type")  type : String? ,//1=>simple , 2=>tax
                                          @Field("myCashQr") myCashQr : String? ,
                                          @Field("zatcaQr") zatcaQr : String? ,
                                          @Field("active") active : String?): Response<InvoicesSettingsDTO>


    @FormUrlEncoded
    @POST("sales/invoice/start_current_invoice_order")
    suspend fun startSaleInvoiceOrderNo(
        @Header("lang") lang : String? ,
        @Header("Authorization") authorization : String? ,
        @Field("current_invoice_order") currentInvoiceOrder : String?
    ): Response<BaseResponseDTO>


    @FormUrlEncoded
    @POST("sales/invoice/start_current_buy_invoice_order")
    suspend fun startPurchaseInvoiceOrderNo(
        @Header("lang") lang : String? ,
        @Header("Authorization") authorization : String? ,
        @Field("current_buy_invoice_order") currentBuyInvoiceOrder : String?
    ): Response<BaseResponseDTO>
}