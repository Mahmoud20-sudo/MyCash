package com.codeIn.common.nearpay

import android.view.View
import io.nearpay.sdk.utils.enums.AuthenticationData
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.TransactionData
import io.nearpay.sdk.utils.listeners.PurchaseListener
import java.util.UUID

fun purchaseNearPay(view: View, money: String, customerReferenceNumber: String?, phone: String,
                    listener: NearPayPayment.Companion.Listener, cashPrice : String? = null, visaPrice : String? = null ) {
    val value = NearPayPayment.prepareValue(money)
    val amount: Long = value// [Required] ammount you want to set .
    val enableReceiptUi = true// [optional] true will enable the ui and false will disable
    val enableReversal = true // it will allow you to enable or disable the reverse button
    val finishTimeOut: Long = 10 // Add the number of seconds
    val transactionId = UUID.randomUUID(); // [optional] You can add your UUID here which allows you to ask about the transaction again using the same UUID
    val enableUiDismiss = true // [optional] it will allow you to control dismissing the UI

    val nearPay = NearPayPayment.setAuthenticationNearPay(view, phone)
    nearPay.purchase(amount, customerReferenceNumber, enableReceiptUi, enableReversal, finishTimeOut, transactionId, enableUiDismiss,
        object : PurchaseListener {
            override fun onPurchaseApproved(transactionData: TransactionData) {
                nearPay.close()
                listener.onPurchaseProcess(0 , transactionData , null , cashPrice , visaPrice )
            }

            override fun onPurchaseFailed(purchaseFailure: PurchaseFailure) {
                nearPay.close()
                when (purchaseFailure) {
                    is PurchaseFailure.PurchaseDeclined -> {
                        // when the payment declined.
                        listener.onPurchaseProcess(1 , null , purchaseFailure ,  cashPrice , visaPrice   )
                    }

                    is PurchaseFailure.PurchaseRejected -> {
                        // when the payment is rejected .
                        listener.onPurchaseProcess(2 , null , null ,  cashPrice , visaPrice )
                    }

                    is PurchaseFailure.AuthenticationFailed -> {
                        // when the authentication failed .
                        // You can use the following method to update your JWT
                        nearPay.updateAuthentication(AuthenticationData.Jwt("JWT HERE"))
                        listener.onPurchaseProcess(3 , null , null , cashPrice , visaPrice  )
                    }

                    is PurchaseFailure.InvalidStatus -> {
                        // Please note that you can get the status using purchaseFailure.status
                        listener.onPurchaseProcess(4 , null , null ,  cashPrice , visaPrice  )
                    }

                    is PurchaseFailure.GeneralFailure -> {
                        // when there is General error .
                        listener.onPurchaseProcess(5 , null , null , cashPrice , visaPrice  )
                    }

                    is PurchaseFailure.UserCancelled -> listener.onPurchaseProcess(6 , null , null , cashPrice , visaPrice  )
                }
            }

        })
}