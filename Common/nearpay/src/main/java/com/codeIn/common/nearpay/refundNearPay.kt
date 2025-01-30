package com.codeIn.common.nearpay

import android.view.View
import com.codeIn.common.nearpay.NearPayPayment.Companion.prepareValue
import com.codeIn.common.nearpay.NearPayPayment.Companion.setAuthenticationNearPay
import io.nearpay.sdk.utils.enums.AuthenticationData
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.TransactionData
import io.nearpay.sdk.utils.listeners.RefundListener
import java.util.UUID
import io.nearpay.sdk.utils.listeners.PurchaseListener

fun refundNearPay(view : View, phone : String, money : String, rnnRefund : String, customerReferenceNumber : String?, listener: NearPayPayment.Companion.Listener,
                  cashPrice: String?, visaPrice: String?, paymentType: Int,) {
            val amount = prepareValue(money)
            val enableReceiptUi = true // [optional] true will enable the ui and false will disable
            val enableReversal = true // it will allow you to enable or disable the reverse button
            val enableEditableRefundAmountUi = true // [optional] true will enable the ui and false will disable
            val finishTimeOut : Long = 10 // Add the number of seconds
            val transactionId = UUID.randomUUID(); // [optional] You can add your UUID here which allows you to ask about the transaction again using the same UUID
            val enableUiDismiss = true // [optional] it will allow you to control dismissing the UI
            val adminPin = "0000"; //[optional] when you add the admin pin here , the UI for admin pin won't be shown.
            val nearPay = setAuthenticationNearPay(view , phone)

            nearPay.refund(amount, rnnRefund, customerReferenceNumber, enableReceiptUi, enableReversal, enableEditableRefundAmountUi, finishTimeOut, transactionId, adminPin, enableUiDismiss, object :
                RefundListener {
                override fun onRefundApproved(transactionData: TransactionData) {
                    // if you wish to get the receipt in Json format use nearPay.toJson()
                    nearPay.close()

                    listener.onRefundProcess(0 ,transactionData , null  , cashPrice, visaPrice, paymentType)
                }
                override fun onRefundFailed(refundFailure: RefundFailure) {

                    nearPay.close()
                    when (refundFailure) {
                        is RefundFailure.RefundDeclined -> {
                            // when the refund is declined
                            listener.onRefundProcess(1 ,null , refundFailure  , cashPrice, visaPrice, paymentType)
                        }
                        is RefundFailure.RefundRejected -> {
                            // when the refund is rejected
                            listener.onRefundProcess(2 ,null , refundFailure , cashPrice, visaPrice, paymentType )
                        }
                        is RefundFailure.AuthenticationFailed -> {
                            // when the Authentication is failed
                            // You can use the following method to update your JWT
                            listener.onRefundProcess(3 ,null , refundFailure , cashPrice, visaPrice, paymentType )
                            nearPay.updateAuthentication(AuthenticationData.Jwt("JWT HERE"))
                        }
                        is RefundFailure.InvalidStatus -> {
                            // you can get the status using refundFailure.status  .
                            listener.onRefundProcess(4 ,null , refundFailure , cashPrice, visaPrice, paymentType )
                        }
                        is RefundFailure.GeneralFailure -> {
                            // when there is general error .
                            listener.onRefundProcess(5 ,null , refundFailure , cashPrice, visaPrice, paymentType )
                        }

                        else -> {
                            nearPay.close()
                        }
                    }
                }
            })

        }