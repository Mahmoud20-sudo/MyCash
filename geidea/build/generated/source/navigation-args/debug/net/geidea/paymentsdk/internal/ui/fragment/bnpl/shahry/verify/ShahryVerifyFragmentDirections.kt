package net.geidea.paymentsdk.`internal`.ui.fragment.bnpl.shahry.verify

import android.os.Bundle
import androidx.navigation.NavDirections
import kotlin.Int
import kotlin.String
import net.geidea.paymentsdk.GdShahryGraphDirections
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs

public class ShahryVerifyFragmentDirections private constructor() {
  private data class GdActionGdInputidfragmentToGdConfirmfragment(
    public val customerIdentifier: String,
  ) : NavDirections {
    public override val actionId: Int = R.id.gd_action_gd_inputidfragment_to_gd_confirmfragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("customerIdentifier", this.customerIdentifier)
        return result
      }
  }

  public companion object {
    public fun gdActionGdInputidfragmentToGdConfirmfragment(customerIdentifier: String):
        NavDirections = GdActionGdInputidfragmentToGdConfirmfragment(customerIdentifier)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdShahryGraphDirections.gdActionGlobalReceipt(args)
  }
}
