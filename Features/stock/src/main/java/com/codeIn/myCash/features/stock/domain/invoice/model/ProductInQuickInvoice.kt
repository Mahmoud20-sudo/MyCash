package com.codeIn.myCash.features.stock.domain.invoice.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductInQuickInvoice (
    var name : String? ,
    var quantity : String? ,
    var price : String? ,
    var taxAvailable : String? ,
    var taxType : String?
) : Parcelable