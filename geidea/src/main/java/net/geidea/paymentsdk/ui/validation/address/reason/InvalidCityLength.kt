package net.geidea.paymentsdk.ui.validation.address.reason

import android.content.Context
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.ui.validation.InvalidationReason

object InvalidCityLength : InvalidationReason {
    override fun getMessage(context: Context): CharSequence {
        return context.getString(R.string.gd_invalid_address_city)
    }
}