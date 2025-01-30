package net.geidea.paymentsdk.`internal`.ui.fragment.bnpl.souhoola.otp

import androidx.navigation.NavDirections
import net.geidea.paymentsdk.GdSouhoolaGraphDirections
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs
import net.geidea.paymentsdk.`internal`.ui.widget.Step

public class SouhoolaOtpFragmentDirections private constructor() {
  public companion object {
    public fun gdActionGlobalGdSouhoolaotpfragment(step: Step): NavDirections =
        GdSouhoolaGraphDirections.gdActionGlobalGdSouhoolaotpfragment(step)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdSouhoolaGraphDirections.gdActionGlobalReceipt(args)
  }
}
