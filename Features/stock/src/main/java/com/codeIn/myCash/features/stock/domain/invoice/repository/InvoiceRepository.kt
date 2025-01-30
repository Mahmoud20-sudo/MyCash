package com.codeIn.myCash.features.stock.domain.invoice.repository

import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import retrofit2.http.Field

interface InvoiceRepository {

    suspend fun makeInvoice(
        cashPrice : String?,
        visaPrice : String?,
        paymentType : Int,
        products : ProductsInCart?,
        nextData : String?,
    ): InvoiceState

    suspend fun makeQuickInvoice(
        cashPrice : String?,
        visaPrice : String?,
        paymentType : Int,
        products : ProductsInCartInQuickInvoice?,
        nextData : String?,
        product : ProductInQuickInvoice?,
        runRefundInvoice : String?,
        codeRefundInvoice : String?,
        dateRefundInvoice : String?,
    ): InvoiceState

    suspend fun getInvoices(
        type : String? ,
        isReturn : String? ,
        clientId : String? ,
        invoiceType : String? ,
        saleOrBuy: String?,
        paymentStatus : String? ,
        limit : String?,
        date : String?
    ) : InvoiceState

    suspend fun getSingleInvoice(
        invoiceId: String?
    ): InvoiceState

    suspend fun getSingleInvoiceWithNumber(
        invoiceNumber: String?
    ): InvoiceState

    suspend fun getProductParameterInInvoice(
        products : ProductsInCart?,
    ): String?
    suspend fun getProductParameterInQuickInvoice(
        products : ProductsInCartInQuickInvoice?,
    ): String?
    suspend fun getProductParameterInReturnInvoice(
        products : ProductInReturnInvoice?,
    ): String?

    suspend fun refundInvoice(
        cashPrice: String?,
        visaPrice: String?,
        paymentType: Int,
        products: ProductInReturnInvoice?,
        clientId : String? ,
        invoiceId : String? ,
        invoiceType : Int ,
        codeRefundInvoice : String? ,
        runRefundInvoice : String? ,
        dateRefundInvoice : String? ,
        mainTypeInvoice: Int
    ): InvoiceState

    suspend fun confirmVisa(
        invoiceId : String?,
        runRefundInvoice : String?,
        codeRefundInvoice : String?,
        dateRefundInvoice : String?,
    ) : InvoiceState
}