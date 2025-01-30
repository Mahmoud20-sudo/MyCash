package com.codeIn.myCash.features.stock.domain.invoice.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PurchaseInvoiceModel (
    var referenceNumber : String? ,
    var date : String? ,
    var note : String?
) : Parcelable