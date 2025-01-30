package com.codeIn.myCash.features.stock.domain.invoice.model

import android.os.Parcelable
import com.codeIn.common.data.Discount
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountInInvoiceModel (
    var discountType : Discount = Discount.None ,
    var discountValue : String? = "" ,
) : Parcelable