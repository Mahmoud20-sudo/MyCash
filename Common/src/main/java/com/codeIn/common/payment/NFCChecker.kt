package com.codeIn.common.payment

import android.content.Context
import android.nfc.NfcManager
import android.util.Log
import com.codeIn.common.printer.DeviceIntegration

class NFCChecker {
    companion object{
        fun checkNFC(context: Context) :Int{
            val manager = context.getSystemService(Context.NFC_SERVICE) as NfcManager
            val adapter = manager.defaultAdapter
            return if (adapter != null && adapter.isEnabled) {
                Log.d("NFC", "Yes NFC available ")
                1
                //Yes NFC available
            } else if (adapter != null && !adapter.isEnabled) {
                Log.d("NFC" , "NFC is not enabled.Need to enable by the user")
                2
                //NFC is not enabled.Need to enable by the user.
            } else {
                Log.d("NFC" , "NFC is not supported ")
                3
                //NFC is not supported
            }
        }
        fun checkNotPOSAndNFC(context: Context): Boolean{
            val device = android.os.Build.MANUFACTURER
            if (DeviceIntegration.values().any { it.name == device })  {
                return true
            }
            else
            {
                val status = checkNFC(context)
                if (status == 1)
                    return true
            }
            return false
        }

        fun checkIfPOS(): Boolean{
            val device = android.os.Build.MANUFACTURER
            return DeviceIntegration.values().any { it.name == device }
        }

        fun checkPhoneNotSupportNFC(context: Context): Boolean{
            val device = android.os.Build.MODEL
            if (!DeviceIntegration.values().any { it.name == device })  {
                val status = checkNFC(context)
                if (status == 3)
                    return true
            }
            return false
        }
    }
}