package com.codeIn.myCash.features.stock.domain.invoice.model

import android.os.Parcelable
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductInReturnInvoice (
    var list : ArrayList<ProductInInvoiceModel>? = ArrayList(),
    var tax : String? = "0.0",
    var discount : String? = "0.0",
    var finalTotal : String = "0.0",
    var initialTotal : String? = "0.0",
    var totalAfterDiscount: String? = "0.0",
    var totalAfterNotification: String? = "0.0"
) : Parcelable