package com.codeIn.myCash.ui.home.products.products.mada

import android.view.View
import com.codeIn.common.nearpay.NearPayPayment
import com.codeIn.common.payment.NFCChecker
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentMadaPaymentBinding
import com.codeIn.myCash.ui.home.products.products.ProductsFragment
import com.codeIn.myCash.utilities.CustomToaster
import io.nearpay.sdk.data.models.ReconciliationReceipt
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.ReconcileFailure
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.TransactionData

fun ProductsFragment.payMada(
     binding : FragmentMadaPaymentBinding ,
     view : View
){
    val amount = binding.amount.text.toString()
    val nearPayListener = object : NearPayPayment.Companion.Listener {
        override fun onPurchaseProcess(
            code: Int,
            transactionData: TransactionData?,
            purchaseFailure: PurchaseFailure?,
            cashValue: String?,
            visaValue: String?
        ) {

            if (code == 0){
                binding.amount.setText("")
                binding.rrn.setText("")
                CustomToaster.show(
                    requireContext() ,
                    getString(R.string.success_message),
                    true
                )
            }
            else
            {
                when (code) {
                    1 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }
                    2 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }
                    3 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.authentication_failed),
                            false
                        )
                    }
                    4 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.general_error),
                            false
                        )
                    }
                    5 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.general_error),
                            false
                        )
                    }
                }
            }
        }

        override fun onRefundProcess(
            code: Int,
            transaction: TransactionData?,
            purchaseFailure: RefundFailure?,
            cashPrice: String?,
            visaPrice: String?,
            paymentType: Int
        ) {

        }

        override fun onReconciliationProcess(
            code: Int,
            receipt: ReconciliationReceipt?,
            reconcileFailure: ReconcileFailure?
        ) {
            TODO("Not yet implemented")
        }

    }

    if (NFCChecker.checkNotPOSAndNFC(requireContext())) {
        if (!amount.isNullOrEmpty() && amount != "0.0" && amount != "0." && amount != "." && amount!!.toDouble() > 0) {
            NearPayPayment.purchaseNearPay(
                view, amount,
                "9ace70b7-977d-4094-b7f4-4ecb17de6753",
                "", nearPayListener
            )

        }

    } else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())) {
        CustomToaster.show(
            requireContext(),
            getString(R.string.this_device_is_not_support_nfc),
            false
        )
    }

}


fun ProductsFragment.refundMada(
    binding : FragmentMadaPaymentBinding ,
    view : View
){
    val amount = binding.amount.text.toString()
    val rrn = binding.rrn.text.toString()
    val nearPayListener = object : NearPayPayment.Companion.Listener {
        override fun onPurchaseProcess(
            code: Int,
            transactionData: TransactionData?,
            purchaseFailure: PurchaseFailure?,
            cashValue: String?,
            visaValue: String?
        ) {

        }

        override fun onRefundProcess(
            code: Int,
            transaction: TransactionData?,
            purchaseFailure: RefundFailure?,
            cashPrice: String?,
            visaPrice: String?,
            paymentType: Int
        ) {
            if (code == 0){
                binding.amount.setText("")
                binding.rrn.setText("")
                CustomToaster.show(
                    requireContext() ,
                    getString(R.string.success_message),
                    true
                )
            }
            else
            {
                when (code) {
                    1 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }
                    2 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }
                    3 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.authentication_failed),
                            false
                        )
                    }
                    4 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.general_error),
                            false
                        )
                    }
                    5 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.general_error),
                            false
                        )
                    }
                }
            }
        }

        override fun onReconciliationProcess(
            code: Int,
            receipt: ReconciliationReceipt?,
            reconcileFailure: ReconcileFailure?
        ) {
            TODO("Not yet implemented")
        }

    }
    if (NFCChecker.checkNotPOSAndNFC(requireContext())) {
        if (!amount.isNullOrEmpty() && amount != "0.0" && amount != "0." && amount != "." && amount.toDouble() > 0) {
            if (!rrn.isNullOrEmpty() && rrn != " ")
                NearPayPayment.refundNearPay(
                    view, "", amount ,
                    rrn,
                    "9ace70b7-977d-4094-b7f4-4ecb17de6753",
                    nearPayListener , null , null , 1)

        }

    } else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())) {
        CustomToaster.show(
            requireContext(),
            getString(R.string.this_device_is_not_support_nfc),
            false
        )
    }

}

fun ProductsFragment.clear(
    binding : FragmentMadaPaymentBinding ,
    view : View
){
    binding.amount.setText("")
    binding.rrn.setText("")
}

