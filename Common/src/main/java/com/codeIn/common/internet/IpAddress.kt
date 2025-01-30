package com.codeIn.common.internet

import android.content.Context
import android.telephony.TelephonyManager
import java.util.Locale

class IpAddress {
    companion object {

        fun getUserCountry(context: Context): String? {
            try {
                val tm: TelephonyManager =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val simCountry: String = tm.simCountryIso
                if (simCountry != null && simCountry.length == 2) { // SIM country code is available
                    return simCountry.lowercase(Locale.US)
                } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                    val networkCountry: String = tm.networkCountryIso
                    if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                        return networkCountry.lowercase(Locale.US)
                    }
                }
            } catch (e: Exception) {
            }
            return null
        }
    }
}