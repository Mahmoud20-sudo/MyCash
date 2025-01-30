package com.codeIn.myCash.features.stock.data.receipt.remote

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceDTO
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptsDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Receipt {

    @GET("sales/receipt/get")
    suspend fun getReceipts(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Query("invoice_id") invoiceId : String? ,
        @Query("client_id") clientId : String? ,
        @Query("paymentStatus") paymentStatus: String? ,
        @Query("date_type") dateType: String? ,
        @Query("sort") sort: String? ,
        @Query("date_from") dateFrom: String? ,
        @Query("date_to") dateTo: String? ,
        @Query("limit") limit : String?,
        @Query("date") date : String?,
    ): Response<ReceiptsDTO>

    @FormUrlEncoded
    @POST("sales/receipt/pay")
    suspend fun payReceipt(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("receipt_id") receiptId : String?,
        @Field("cashPrice") cashPrice : String?,
        @Field("visaPrice") visaPrice : String?,
        @Field("nextDate") nextDate : String? ,
        @Field("runRefund") runRefundInvoice : String? ,
        @Field("codeRefund") codeRefundInvoice : String? ,
        @Field("dateRefund") dateRefundInvoice : String? ,
    ):Response<InvoiceDTO>
}