package net.geidea.paymentsdk

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import java.io.Serializable
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs
import net.geidea.paymentsdk.`internal`.ui.widget.Step
import net.geidea.paymentsdk.flow.pay.PaymentData

public class GdCardGraphDirections private constructor() {
  private data class GdActionGlobalCardauthfragment(
    public val finalPaymentData: PaymentData,
    public val step: Step? = null,
    public val cvv: String? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.gd_action_global_cardauthfragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        if (Parcelable::class.java.isAssignableFrom(PaymentData::class.java)) {
          result.putParcelable("finalPaymentData", this.finalPaymentData as Parcelable)
        } else if (Serializable::class.java.isAssignableFrom(PaymentData::class.java)) {
          result.putSerializable("finalPaymentData", this.finalPaymentData as Serializable)
        } else {
          throw UnsupportedOperationException(PaymentData::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
          result.putParcelable("step", this.step as Parcelable?)
        } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
          result.putSerializable("step", this.step as Serializable?)
        }
        result.putString("cvv", this.cvv)
        return result
      }
  }

  private data class GdActionGlobalGdPaymentoptionsfragment(
    public val downPaymentAmount: BigDecimal? = null,
    public val step: Step? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.gd_action_global_gd_paymentoptionsfragment

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
    public fun gdActionGlobalCardauthfragment(
      finalPaymentData: PaymentData,
      step: Step? = null,
      cvv: String? = null,
    ): NavDirections = GdActionGlobalCardauthfragment(finalPaymentData, step, cvv)

    public fun gdActionGlobalGdPaymentoptionsfragment(downPaymentAmount: BigDecimal? = null,
        step: Step? = null): NavDirections =
        GdActionGlobalGdPaymentoptionsfragment(downPaymentAmount, step)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdNavigationDirections.gdActionGlobalReceipt(args)
  }
}
