package net.geidea.paymentsdk.`internal`.ui.fragment.bnpl.valu.otp

import androidx.navigation.NavDirections
import net.geidea.paymentsdk.GdValuGraphDirections
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs

public class ValuOtpFragmentDirections private constructor() {
  public companion object {
    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdValuGraphDirections.gdActionGlobalReceipt(args)
  }
}
