package net.geidea.paymentsdk.`internal`.ui.fragment.receipt

import androidx.navigation.NavDirections
import net.geidea.paymentsdk.GdNavigationDirections

public class ReceiptFragmentDirections private constructor() {
  public companion object {
    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdNavigationDirections.gdActionGlobalReceipt(args)
  }
}
