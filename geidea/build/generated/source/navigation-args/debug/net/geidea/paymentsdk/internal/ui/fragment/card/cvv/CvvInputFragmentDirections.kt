package net.geidea.paymentsdk.`internal`.ui.fragment.card.cvv

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import java.io.Serializable
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import net.geidea.paymentsdk.GdCardGraphDirections
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs
import net.geidea.paymentsdk.`internal`.ui.widget.Step
import net.geidea.paymentsdk.flow.pay.PaymentData

public class CvvInputFragmentDirections private constructor() {
  private data class GdActionGdCvvinputfragmentToGdCardauthfragment(
    public val finalPaymentData: PaymentData,
    public val step: Step? = null,
    public val cvv: String? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.gd_action_gd_cvvinputfragment_to_gd_cardauthfragment

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

  public companion object {
    public fun gdActionGdCvvinputfragmentToGdCardauthfragment(
      finalPaymentData: PaymentData,
      step: Step? = null,
      cvv: String? = null,
    ): NavDirections = GdActionGdCvvinputfragmentToGdCardauthfragment(finalPaymentData, step, cvv)

    public fun gdActionGlobalCardauthfragment(
      finalPaymentData: PaymentData,
      step: Step? = null,
      cvv: String? = null,
    ): NavDirections = GdCardGraphDirections.gdActionGlobalCardauthfragment(finalPaymentData, step,
        cvv)

    public fun gdActionGlobalGdPaymentoptionsfragment(downPaymentAmount: BigDecimal? = null,
        step: Step? = null): NavDirections =
        GdCardGraphDirections.gdActionGlobalGdPaymentoptionsfragment(downPaymentAmount, step)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdCardGraphDirections.gdActionGlobalReceipt(args)
  }
}
