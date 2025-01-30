package net.geidea.paymentsdk.`internal`.ui.fragment.bnpl.valu.phone

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import net.geidea.paymentsdk.GdValuGraphDirections
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs
import net.geidea.paymentsdk.`internal`.ui.widget.Step

public class ValuPhoneNumberFragmentDirections private constructor() {
  private data class GdActionGdValuphonenumberfragmentToGdValuinstallmentplanfragment(
    public val customerIdentifier: String,
    public val step: Step,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_valuphonenumberfragment_to_gd_valuinstallmentplanfragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        result.putString("customerIdentifier", this.customerIdentifier)
        if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
          result.putParcelable("step", this.step as Parcelable)
        } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
          result.putSerializable("step", this.step as Serializable)
        } else {
          throw UnsupportedOperationException(Step::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        return result
      }
  }

  public companion object {
    public
        fun gdActionGdValuphonenumberfragmentToGdValuinstallmentplanfragment(customerIdentifier: String,
        step: Step): NavDirections =
        GdActionGdValuphonenumberfragmentToGdValuinstallmentplanfragment(customerIdentifier, step)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdValuGraphDirections.gdActionGlobalReceipt(args)
  }
}
