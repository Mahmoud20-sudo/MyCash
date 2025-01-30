package net.geidea.paymentsdk.`internal`.ui.fragment.bnpl.souhoola.review

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import java.io.Serializable
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import kotlin.Int
import kotlin.Suppress
import net.geidea.paymentsdk.GdSouhoolaGraphDirections
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs
import net.geidea.paymentsdk.`internal`.ui.widget.Step

public class SouhoolaReviewFragmentDirections private constructor() {
  private data class GdActionGdSouhoolareviewfragmentToGdSouhoolaotpfragment(
    public val step: Step,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_souhoolareviewfragment_to_gd_souhoolaotpfragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
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

  private data class GdActionGdSouhoolareviewfragmentToGdPaymentoptionsfragment(
    public val downPaymentAmount: BigDecimal? = null,
    public val step: Step? = null,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_souhoolareviewfragment_to_gd_paymentoptionsfragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        if (Parcelable::class.java.isAssignableFrom(BigDecimal::class.java)) {
          result.putParcelable("downPaymentAmount", this.downPaymentAmount as Parcelable?)
        } else if (Serializable::class.java.isAssignableFrom(BigDecimal::class.java)) {
          result.putSerializable("downPaymentAmount", this.downPaymentAmount as Serializable?)
        }
        if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
          result.putParcelable("step", this.step as Parcelable?)
        } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
          result.putSerializable("step", this.step as Serializable?)
        }
        return result
      }
  }

  public companion object {
    public fun gdActionGdSouhoolareviewfragmentToGdSouhoolaotpfragment(step: Step): NavDirections =
        GdActionGdSouhoolareviewfragmentToGdSouhoolaotpfragment(step)

    public
        fun gdActionGdSouhoolareviewfragmentToGdPaymentoptionsfragment(downPaymentAmount: BigDecimal?
        = null, step: Step? = null): NavDirections =
        GdActionGdSouhoolareviewfragmentToGdPaymentoptionsfragment(downPaymentAmount, step)

    public fun gdActionGlobalGdSouhoolaotpfragment(step: Step): NavDirections =
        GdSouhoolaGraphDirections.gdActionGlobalGdSouhoolaotpfragment(step)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdSouhoolaGraphDirections.gdActionGlobalReceipt(args)
  }
}
