package com.codeIn.myCash.features.stock.domain.receipt.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentReceiptModel (
    var money : String? ,
    var nextDate : String? ,
    var remaining : String?
) : Parcelable