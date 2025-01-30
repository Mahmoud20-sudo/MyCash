package com.codeIn.myCash.features.stock.data.invoice.remote

import com.codeIn.common.data.BaseResponseDTO
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceDTO
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoicesDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Invoice {

    @FormUrlEncoded
    @POST("sales/invoice/make_invoice")
    suspend fun makeInvoice(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("cashPrice") cashPrice : String? ,
        @Field("visaPrice") visaPrice : String? ,
        @Field("paymentType") paymentType : String? ,
        @Field("invoiceType") invoiceType : Int ,
        @Field("client_id") clientId : String? ,
        @Field("products") products : String? ,
        @Field("nextData") nextData : String? ,
        @Field("referenceNumber") referenceNumberPurchaseInvoice : String? ,
        @Field("referenceDate") referenceDatePurchaseInvoice: String? ,
        @Field("note") notePurchaseInvoice : String? ,
        @Field("invoice_id") invoiceIdRefundInvoice : String? ,
        @Field("branch_id") branchId : String? ,
        @Field("saleOrBuy") saleOrBuy : Int,
    ): Response<InvoiceDTO>

    @GET("sales/invoice/single")
    suspend fun getSingleInvoice(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Query("invoice_id") invoiceId : String?
    ): Response<InvoiceDTO>

    @FormUrlEncoded
    @POST("sales/invoice/confirm_visa")
    suspend fun confirmVisaPayment(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("invoice_id") invoiceId : String?,
        @Field("runRefund") runRefundInvoice : String? ,
        @Field("codeRefund") codeRefundInvoice : String? ,
        @Field("dateRefund") dateRefundInvoice : String? ,
    ): Response<InvoiceDTO>

    @GET("sales/invoice/get")
    suspend fun getInvoices(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Query("type") type : String?,
        @Query("isReturn") isReturn : String?,
        @Query("client_id") clientId : String?,
        @Query("invoiceType") invoiceType : String?,
        @Query("saleOrBuy") saleOrBuy : String?,
        @Query("paymentStatus") paymentStatus : String?,
        @Query("limit") limit : String?,
        @Query("date") date : String?,
    ): Response<InvoicesDTO>


    @GET("sales/invoice/singleByInvoiceNumber")
    suspend fun getInvoiceByNumberInvoice(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Query("invoiceNumber")invoiceNumber : String?
    ): Response<InvoiceDTO>

    @FormUrlEncoded
    @POST("sales/invoice/quick_invoice")
    suspend fun makeQuickInvoice(
        @Header("lang") lang : String?,
        @Header("Authorization") authorization:String?,
        @Field("cashPrice") cashPrice : String? ,
        @Field("visaPrice") visaPrice : String? ,
        @Field("paymentType") paymentType : String? ,
        @Field("invoiceType") invoiceType : Int ,
        @Field("client_id") clientId : String? ,
        @Field("products") products : String? ,
        @Field("nextData") nextData : String? ,
        @Field("invoice_id") invoiceIdRefundInvoice : String? ,
        @Field("runRefund") runRefundInvoice : String? ,
        @Field("codeRefund") codeRefundInvoice : String? ,
        @Field("dateRefund") dateRefundInvoice : String? ,
        @Field("saleOrBuy") saleOrBuy : Int,
        @Field("branch_id") branchId : String?
    ):Response<InvoiceDTO>

}