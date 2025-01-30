package net.geidea.paymentsdk.`internal`.ui.fragment.qr.r2p

import androidx.navigation.NavDirections
import net.geidea.paymentsdk.GdNavigationDirections
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs

public class MeezaQrRequestPaymentFragmentDirections private constructor() {
  public companion object {
    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdNavigationDirections.gdActionGlobalReceipt(args)
  }
}
