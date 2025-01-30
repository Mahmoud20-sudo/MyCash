package com.codeIn.myCash.features.user.domain.settings.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentMethodState(
    var initialTotal: String = "0.0",
    var systemDiscount: String = "0.0",
    var discountValue: String = "0.0",
    var discount: String = "",
    var total: String = "0.0",
    var tax: String = "0.0",
    var devicePrice: String = "0.0",
    var packagePrice: String = "0.0"
) : Parcelable