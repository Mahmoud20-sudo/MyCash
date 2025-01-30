package com.codeIn.myCash.features.stock.domain.invoice.model

import android.os.Parcelable
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.common.data.SaleBuyType
import com.codeIn.myCash.features.sales.domain.clients.model.ClientRequest
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductsInCartInQuickInvoice (
    var list : ArrayList<ProductInQuickInvoice>? = ArrayList(),
    var invoiceType : InvoiceType = InvoiceType.SIMPLE,
    var mainTypeInvoice : MainTypeInvoice = MainTypeInvoice.SALE,
    var clientRequest: ClientRequest? = null ,
    var clientId : String? = "" ,
    var tax : String? = "0.0",
    var count : Int = 0,
    var finalTotal : String? = "0.0",
    var initialTotal : String? = "0.0",
    var saleOrBuy: SaleBuyType = SaleBuyType.SALE
) : Parcelable