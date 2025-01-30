package net.geidea.paymentsdk.`internal`.ui.fragment.bnpl.souhoola.verify

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import net.geidea.paymentsdk.GdSouhoolaGraphDirections
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs
import net.geidea.paymentsdk.`internal`.ui.widget.Step

public class SouhoolaVerifyCustomerFragmentDirections private constructor() {
  public companion object {
    public fun gdActionGdSouhoolaverifycustomerfragmentToGdSouhoolainstallmentplanfragment():
        NavDirections =
        ActionOnlyNavDirections(R.id.gd_action_gd_souhoolaverifycustomerfragment_to_gd_souhoolainstallmentplanfragment)

    public fun gdActionGlobalGdSouhoolaotpfragment(step: Step): NavDirections =
        GdSouhoolaGraphDirections.gdActionGlobalGdSouhoolaotpfragment(step)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdSouhoolaGraphDirections.gdActionGlobalReceipt(args)
  }
}
