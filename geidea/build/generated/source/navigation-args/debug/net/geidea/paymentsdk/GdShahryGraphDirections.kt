package net.geidea.paymentsdk

import androidx.navigation.NavDirections
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs

public class GdShahryGraphDirections private constructor() {
  public companion object {
    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdNavigationDirections.gdActionGlobalReceipt(args)
  }
}
