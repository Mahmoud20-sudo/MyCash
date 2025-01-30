package com.codeIn.common.payment

import android.content.Context
import com.codeIn.common.R
import com.codeIn.common.domain.PaymentFactory
import com.codeIn.common.printer.DeviceIntegration
import javax.inject.Inject

class PaymentFactoryImpl @Inject constructor() : PaymentFactory {
    override fun payment(context: Context, amount: String, phone: String) {
        val device = android.os.Build.MODEL
        if (DeviceIntegration.values().any { it.name == device }) {
            when (device) {
                DeviceIntegration.BLU.toString() -> {
                    //Blu
                }

                DeviceIntegration.QUALCOMM.toString() -> {
                    //surePay
                }

                DeviceIntegration.VSTC.toString() -> {
                    //nearPay
                }

                DeviceIntegration.lephone.toString() -> {
                    // Geidea
                }

                DeviceIntegration.Wiseasy.toString() -> {
                    //Pioneers
                }

                DeviceIntegration.newland.toString() -> {
                    //newLeab
                }

                else -> {
                }
            }
        } else {
        }
    }

    override fun handleFailureCode(context: Context, failureLink: String): String? = try {
        val code = failureLink.split("id=")[1].toInt()
        when (code) {
            PaymentFailureCode.USER_CANCEL.ordinal -> null
            else -> context.getString(R.string.error)

        }
    } catch (e: Exception) {
        context.getString(R.string.error)
    }
}

enum class PaymentFailureCode(code: Int) {
    USER_CANCEL(1036)
}