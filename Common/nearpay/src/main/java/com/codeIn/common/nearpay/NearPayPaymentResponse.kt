package com.codeIn.common.nearpay

import android.os.Parcelable
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.TransactionData
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize


data class NearPayPaymentResponse (

    var responseCode : Int,
    var transactionData : TransactionData?,
    var purchaseFailure: PurchaseFailure?,
    var refundFailure: RefundFailure?,
)