package com.codeIn.myCash.features.stock.domain.memorandum.model

import android.os.Parcelable
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.ProductModelInMemorandum
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductInInvoiceMemorandum (
    var list : ArrayList<ProductModel>? = ArrayList(),
    var tax : String? = "0.0",
    var finalTotal : String? = "0.0",
    var initialTotal : String? = "0.0",
) : Parcelable