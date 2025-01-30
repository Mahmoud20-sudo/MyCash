package com.codeIn.common.nearpay

import android.util.Log
import android.view.View
import io.nearpay.sdk.Environments
import io.nearpay.sdk.NearPay
import io.nearpay.sdk.data.models.ReconciliationReceipt
import io.nearpay.sdk.data.models.TransactionReceipt
import io.nearpay.sdk.utils.PaymentText
import io.nearpay.sdk.utils.enums.AuthenticationData
import io.nearpay.sdk.utils.enums.NetworkConfiguration
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.ReconcileFailure
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.SetupFailure
import io.nearpay.sdk.utils.enums.TransactionData
import io.nearpay.sdk.utils.enums.UIPosition
import io.nearpay.sdk.utils.listeners.PurchaseListener
import io.nearpay.sdk.utils.listeners.ReconcileListener
import io.nearpay.sdk.utils.listeners.RefundListener
import io.nearpay.sdk.utils.listeners.SetupListener
import io.nearpay.sdk.utils.toImage
import java.util.Locale
import java.util.UUID

class NearPayPayment {

    companion object{
        fun setAuthenticationNearPay(view: View, phone: String , lang : String? =  "ar"): NearPay {
            val auth = AuthenticationData.Mobile("+966599007745")
            return NearPay.Builder()
                .context(view.context)
                .authenticationData(auth)
                .environment(if(BuildConfig.FLAVOR == "staging") Environments.SANDBOX else Environments.PRODUCTION)
                .locale(Locale(lang))
                .networkConfiguration(NetworkConfiguration.SIM_PREFERRED)
                .uiPosition(UIPosition.CENTER_BOTTOM)
                .paymentText(PaymentText("يرجى تمرير الطاقة", "please tap your card"))
                .loadingUi(true)
                .build()

        }

        fun purchaseNearPay(view: View, money: String, customerReferenceNumber: String?, phone: String ,
                            listener: Listener , cashPrice : String? = null , visaPrice : String? = null ) {
            val value = prepareValue(money)
            val amount: Long = value// [Required] ammount you want to set .
            val enableReceiptUi = true// [optional] true will enable the ui and false will disable
            val enableReversal = true // it will allow you to enable or disable the reverse button
            val finishTimeOut: Long = 10 // Add the number of seconds
            val transactionId = UUID.randomUUID(); // [optional] You can add your UUID here which allows you to ask about the transaction again using the same UUID
            val enableUiDismiss = true // [optional] it will allow you to control dismissing the UI

            val nearPay = setAuthenticationNearPay(view, phone)

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

        fun printReceipt(receipt : TransactionReceipt , view: View){
            receipt?.toImage(view.context , view.width , 18 ) {

            }
        }


        fun refundNearPay(view : View , phone : String , money : String , rnnRefund : String , customerReferenceNumber : String? , listener: Listener,
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

        fun prepareValue(value : String): Long{
            var subValue = "0.0"
            var subValueAfterDot = "0.0"
            if (value.contains("."))
            {
                val index : Int = value.indexOf(".")
                subValue = value.substring(0 , index)
                subValueAfterDot  = value.substring(index+1 , value.length)
                if (subValueAfterDot.length == 1) subValueAfterDot += "0"
            }
            else
            {
                subValue = value
                subValueAfterDot = "00"
            }
            return  (subValue+subValueAfterDot).toLong()
        }

        fun reconciliationNearPay(view: View  , phone : String , listener: Listener ){
            val nearPay = setAuthenticationNearPay(view, phone)
            val enableReceiptUi = true //[optional] true will enable the ui and false will disable.
            val finishTimeOut : Long = 10 //[optional] Add the number of seconds.
            val enableUiDismiss = true // [optional] it will allow you to control dismissing the UI
            val adminPin = "0000"; //[optional] when you add the admin pin here , the UI for admin pin won't be shown.
            val transactionId = UUID.randomUUID(); // [optional] You can add your UUID here which allows you to ask about the transaction again using the same UUID

            nearPay.reconcile(transactionId, enableReceiptUi, adminPin, finishTimeOut, enableUiDismiss, object :
                ReconcileListener {
                override fun onReconcileFinished(receipt: ReconciliationReceipt?) {
                    nearPay.close()
                    listener.onReconciliationProcess(0, receipt , null )
                }

                override fun onReconcileFailed(reconcileFailure: ReconcileFailure) {
                    when (reconcileFailure) {
                        is ReconcileFailure.FailureMessage -> {
                            // when there is FailureMessage
                            // Your Code Here
                            nearPay.close()
                            listener.onReconciliationProcess(1, null , reconcileFailure )
                        }

                        is ReconcileFailure.AuthenticationFailed -> {
                            // when the Authentication is failed
                            // You can use the following method to update your JWT
                            nearPay.close()
                            listener.onReconciliationProcess(2, null , reconcileFailure )
                            nearPay.updateAuthentication(AuthenticationData.Jwt("JWT HERE"))
                        }

                        is ReconcileFailure.InvalidStatus -> {
                            // you can get the status using reconcileFailure.status
                            // Your Code Here
                            nearPay.close()
                            listener.onReconciliationProcess(3, null , reconcileFailure )
                        }

                        is ReconcileFailure.GeneralFailure -> {
                            // when there is unexpected error .
                            // Your Code Here
                            nearPay.close()
                            listener.onReconciliationProcess(4, null , reconcileFailure )
                        }

                        else ->{
                            nearPay.close()
                            listener.onReconciliationProcess(5, null , reconcileFailure )
                        }
                    }
                }
            })
        }

        interface Listener {
            fun onPurchaseProcess(code : Int , transaction : TransactionData? , purchaseFailure: PurchaseFailure? , cashPrice: String? , visaPrice: String?)
            fun onRefundProcess(code : Int , transaction : TransactionData? , purchaseFailure: RefundFailure? , cashPrice: String?, visaPrice: String?, paymentType: Int,)
            fun onReconciliationProcess(code : Int , receipt : ReconciliationReceipt?, reconcileFailure: ReconcileFailure?)
        }
    }
}