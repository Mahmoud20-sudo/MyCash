package com.codeIn.myCash.features.stock.domain.memorandum.model

data class ProductInMemorandumInvoiceRequest (
    var product_id : Int ,
    var quantity : Int ,
    var price : String?
)