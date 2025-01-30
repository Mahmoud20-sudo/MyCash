package net.geidea.paymentsdk.`internal`.ui.fragment.bnpl.valu.plan

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import java.io.Serializable
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import kotlin.Int
import kotlin.Suppress
import net.geidea.paymentsdk.GdValuGraphDirections
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs
import net.geidea.paymentsdk.`internal`.ui.widget.Step

public class ValuInstallmentPlanFragmentDirections private constructor() {
  private data class GdActionGdValuinstallmentplanfragmentToGdValuotpfragment(
    public val step: Step,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_valuinstallmentplanfragment_to_gd_valuotpfragment

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

  private data class GdActionGdValuinstallmentplanfragmentToGdPaymentoptionsfragment(
    public val downPaymentAmount: BigDecimal? = null,
    public val step: Step? = null,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_valuinstallmentplanfragment_to_gd_paymentoptionsfragment

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
    public fun gdActionGdValuinstallmentplanfragmentToGdValuotpfragment(step: Step): NavDirections =
        GdActionGdValuinstallmentplanfragmentToGdValuotpfragment(step)

    public
        fun gdActionGdValuinstallmentplanfragmentToGdPaymentoptionsfragment(downPaymentAmount: BigDecimal?
        = null, step: Step? = null): NavDirections =
        GdActionGdValuinstallmentplanfragmentToGdPaymentoptionsfragment(downPaymentAmount, step)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdValuGraphDirections.gdActionGlobalReceipt(args)
  }
}
