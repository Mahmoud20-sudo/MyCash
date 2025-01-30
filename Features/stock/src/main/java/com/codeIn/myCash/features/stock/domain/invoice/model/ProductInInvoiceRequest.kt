package com.codeIn.myCash.features.stock.domain.invoice.model

data class ProductInInvoiceRequest (
    var product_id : Int ,
    var quantity : Int ,
    var invoiceDiscountValue : String? ,
    var invoiceDiscountType : Int ,
)
